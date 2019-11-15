package scc.DbResources;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import resources.Database.DatabaseConnector;
import resources.Database.resources.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/users")
public class UserResource {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();
    private String UsersCollection = db.getCollectionString("Users");

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Users getUser(@PathParam("id") String id) {

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(UsersCollection,
                String.format("SELECT * FROM Users u WHERE u.id = '%s'", id), queryOptions).toBlocking()
                .getIterator();

        if (it.hasNext())
            for (Document d : it.next().getResults()) {
                System.out.println(d.toJson());
                Gson g = new Gson();
                Users u = g.fromJson(d.toJson(), Users.class);
                return u;
            }

        return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public List<Users> getUsers(){

		List<Users> z = new ArrayList<>();

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Users"),
                queryOptions).toBlocking().getIterator();
        

        	while( it.hasNext())
			for( Document d : it.next().getResults()) {
				System.out.println( d.toJson());
				Gson g = new Gson();
				Users u = g.fromJson(d.toJson(), Users.class);
                z.add(u);
			}

        return z;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUser(Users user) {

        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Users");
        client.createDocument(collectionLink, user, null, true).toCompletable().await();

        return user.getId();

    }


}