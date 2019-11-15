package scc.DbResources;

import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import resources.Database.DatabaseConnector;
import resources.Database.resources.Communities;

        import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;

@Path("/community")
public class CommunityResource
{

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();

    private String UsersCollection = db.getCollectionString("Communities");
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Communities getCommunity(@PathParam("id") String id){


        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Communities c WHERE c.id = '%s'", id),
                queryOptions).toBlocking().getIterator();

        if( it.hasNext())
			for( Document d : it.next().getResults()) {
				System.out.println( d.toJson());
				Gson g = new Gson();
				Communities u = g.fromJson(d.toJson(), Communities.class);
                return u;
			}

        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public void addCommunity(Communities community){

        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Communities");
        client.createDocument(collectionLink, community, null, true)
                .toCompletable()
                .await();

    }

}
