package scc.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import resources.Database.DatabaseConnector;
import resources.Database.entities.Posts;


@Path("/home")
public class InitialPage {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();
    private String UsersCollection = db.getCollectionString("Posts");

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public List<Posts> getUsers(){

		List<Posts> z = new ArrayList<>();

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Posts"),
                queryOptions).toBlocking().getIterator();
        

        	while( it.hasNext())
			for( Document d : it.next().getResults()) {
				System.out.println( d.toJson());
				Gson g = new Gson();
				Posts u = g.fromJson(d.toJson(), Posts.class);
                z.add(u);
			}

        return z;
    }
}