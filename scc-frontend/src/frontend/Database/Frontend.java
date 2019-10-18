package frontend.Database;

import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import io.reactivex.netty.client.RxClient;
import java.net.URI;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.*;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

public class Frontend {
    private static DatabaseConnector dbconnector;
    private static AsyncDocumentClient client;

    public static void main(String[] args){
        dbconnector = new DatabaseConnector();
        client = dbconnector.getDocumentClient();
    }

    public void initialPage(){
        // as this is a multi collection enable cross partition query
        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        String collectionLink = dbconnector.getCollectionString("Post");

        Iterator<FeedResponse<Document>> it = client.queryDocuments(collectionLink, "SELECT * FROM Family WHERE Family.lastName = 'Andersen'", options).toBlocking().getIterator();
    }
}
