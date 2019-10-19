package scc.DbResources;

import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import resources.Database.DatabaseConnector;
import resources.Database.resources.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;

@Path("/users")
public class UserResource {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();
    private String UsersCollection = db.getCollectionString("Users");

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Document getUser(@PathParam("id") String id){


        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Users WHERE id = %s", id),
                queryOptions).toBlocking().getIterator();

        for( Document d : it.next().getResults())
            return d;

        return null;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public void addUser(Users user){

        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Users");
        client.createDocument(collectionLink, user, null, true)
                .toCompletable()
                .await();

    }

}