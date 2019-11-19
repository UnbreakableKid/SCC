package war;


import java.util.Iterator;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import redis.clients.jedis.Jedis;
import war.DatabaseConnector;
import war.RedisConnector;
import war.entities.Posts;


public class Function {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java&code={your function key}
     * 2. curl "{your host}/api/HttpTrigger-Java?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, it is used to invoke function deployed to Azure.
     * More details: https://aka.ms/functions_authorization_keys
     */
    @FunctionName("refresh-cache")
    public void cosmosFunction(@TimerTrigger(name = "refreshCache", schedule = "0 * * * * *") String timerInfo,
            ExecutionContext context) {
        try (Jedis jedis = new RedisConnector().createClient();
        ) {

            Long cnt = 0L;
            DatabaseConnector db = new DatabaseConnector();
            AsyncDocumentClient client = db.getDocumentClient();
            String UsersCollection = db.getCollectionString("Posts");
            FeedOptions queryOptions = new FeedOptions();
            
            queryOptions.setEnableCrossPartitionQuery(true);
            queryOptions.setMaxDegreeOfParallelism(-1);

            jedis.flushAll();

            Iterator<FeedResponse<Document>> it = client.queryDocuments(UsersCollection,
                    String.format("SELECT * FROM Posts p ORDER BY p._ts ASC"), queryOptions).toBlocking().getIterator();

            while (it.hasNext())
                for (Document d : it.next().getResults()) {
                    System.out.println("WENT TO DATABASE");
                    Gson g = new Gson();
                    Posts u = g.fromJson(d.toJson(), Posts.class);
                    String json = new Gson().toJson(u);

                    cnt = jedis.lpush("MostRecentPosts", json);
                }

        }
        }
}
