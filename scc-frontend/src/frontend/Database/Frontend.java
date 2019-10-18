package frontend.Database;

import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import io.reactivex.netty.client.RxClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        dbconnector = new DatabaseConnector();
        client = dbconnector.getDocumentClient();


        String line;
        while(true){
            line = reader.readLine();
            String[] command = line.split("\\s+");
            switch(command[0]){
                case "initialPage":
                    initialPage();
                    break;
                case "thread":
                    getThread(command[1]);
                    break;
                case "like":
                    likePost(command[1]);
                    break;

            }
        }
    }

    private static void initialPage(){
        // as this is a multi collection enable cross partition query
        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        String collectionLink = dbconnector.getCollectionString("Posts");

        Iterator<FeedResponse<Document>> it = client.queryDocuments(collectionLink, "SELECT * FROM Posts", options).toBlocking().getIterator();

        //TODO do something with the posts

    }

    public static void getThread(String id){

    }

    public static void likePost(String id){

    }
}
