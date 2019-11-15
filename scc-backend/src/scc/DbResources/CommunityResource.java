package scc.DbResources;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import resources.Database.DatabaseConnector;
import resources.Database.entities.Communities;

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

    public String addCommunity(Communities community){

        String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Communities");
        client.createDocument(collectionLink, community, null, true)
                .toCompletable()
                .await();
        return community.getId();
    }

}
