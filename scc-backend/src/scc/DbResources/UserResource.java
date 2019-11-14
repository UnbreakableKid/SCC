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
import java.util.Iterator;
import java.util.UUID;

@Path("/users")
public class UserResource {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();
    private String UsersCollection = db.getCollectionString("Users");

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Users getUser(@PathParam("id") String id){


        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Users u WHERE u.name = '%s'", id),
                queryOptions).toBlocking().getIterator();

        	if( it.hasNext())
			for( Document d : it.next().getResults()) {
				System.out.println( d.toJson());
				Gson g = new Gson();
				Users u = g.fromJson(d.toJson(), Users.class);
                return u;
			}

        return null;
    }

    @POST
    @Path("/username={u}")
    @Consumes(MediaType.APPLICATION_JSON)

    public void addUser(@PathParam("u") String name) {

        Users user = new Users();
        UUID id = UUID.randomUUID();
        user.setName(name);
        user.setId(id);
        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Users");
        client.createDocument(collectionLink, user, null, true)
                .toCompletable()
                .await();

                

    }

}