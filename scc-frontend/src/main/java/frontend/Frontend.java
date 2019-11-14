package main.java.frontend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import main.java.frontend.Database.DatabaseConnector;
import main.java.frontend.Database.resources.Posts;


public class Frontend {
    private static DatabaseConnector db = new DatabaseConnector();
    private static AsyncDocumentClient client = db.getDocumentClient();

    private static String UsersCollection = db.getCollectionString("Users");

    public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    


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
     
        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Posts"),
                queryOptions).toBlocking().getIterator();

    
        int i = 0;
        while( it.hasNext() && i < 10)
            for( Document d : it.next().getResults()) {
                    System.out.println( d.toJson());
                    Gson g = new Gson();
                    Posts u = g.fromJson(d.toJson(), Posts.class);
                    System.out.println(u);
                    i++;
            }

    }

    public static void getThread(String id){

    }

    public static void likePost(String id){

    }
}
