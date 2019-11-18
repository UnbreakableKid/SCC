package scc.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import redis.clients.jedis.Jedis;
import resources.Database.DatabaseConnector;
import resources.Database.RedisConnector;
import resources.Database.entities.Posts;

@Path("/pages/initial")
public class InitialPage {

    Boolean USINGCACHE = true;
    
    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();
    private String UsersCollection = db.getCollectionString("Posts");

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public List<String> getInitialPage() {
        
        List<String> z = new ArrayList<>();
        
        if (USINGCACHE) {
            
            Jedis jedis = new RedisConnector().createClient();
            List<String> lst = jedis.lrange("MostRecentPosts", 0, 5);
            if (!lst.isEmpty()) {

                System.out.println("WENT TO CACHE");
                return lst;

            }

            else {
                Long cnt = 0L;
                FeedOptions queryOptions = new FeedOptions();
                queryOptions.setEnableCrossPartitionQuery(true);
                queryOptions.setMaxDegreeOfParallelism(-1);

                Iterator<FeedResponse<Document>> it = client
                        .queryDocuments(UsersCollection, String.format("SELECT * FROM Posts p ORDER BY p._ts ASC"), queryOptions)
                        .toBlocking().getIterator();

                while (it.hasNext())
                    for (Document d : it.next().getResults()) {
                        System.out.println("WENT TO DATABASE");
                        Gson g = new Gson();
                        Posts u = g.fromJson(d.toJson(), Posts.class);
                        String json = new Gson().toJson(u);

                        cnt = jedis.lpush("MostRecentPosts", json);
                    }
               
                if (cnt > 5)
                    jedis.ltrim("MostRecentPosts", 0, 5);

                lst = jedis.lrange("MostRecentPosts", 0, 5);
                return lst;

            }
        }
        else {
              FeedOptions queryOptions = new FeedOptions();
                queryOptions.setEnableCrossPartitionQuery(true);
                queryOptions.setMaxDegreeOfParallelism(-1);

                Iterator<FeedResponse<Document>> it = client
                        .queryDocuments(UsersCollection, String.format("SELECT * FROM Posts p ORDER BY p._ts ASC"), queryOptions)
                        .toBlocking().getIterator();

                while (it.hasNext())
                for (Document d : it.next().getResults()) {
                    System.out.println(d.toJson());
                    Gson g = new Gson();
                    Posts u = g.fromJson(d.toJson(), Posts.class);
                    String json = new Gson().toJson(u);
                    z.add(json);
                }
                    
            return z;
        }
    }
}