package scc.DbResources;

import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import resources.Database.DatabaseConnector;
import resources.Database.resources.Posts;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;

@Path("/post")
public class PostResource {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();

    private String UsersCollection = db.getCollectionString("Users");
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Document getPost(@PathParam("id") String id){


        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Posts WHERE id = %s", id),
                queryOptions).toBlocking().getIterator();

        for( Document d : it.next().getResults())
            return d;

        return null;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public void addPost(Posts post){

        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Posts");
        client.createDocument(collectionLink, post, null, true)
                .toCompletable()
                .await();

    }

}
