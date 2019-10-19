package main.java.frontend;

import com.microsoft.azure.cosmosdb.*;

import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import main.java.Database.DatabaseConnector;
import main.java.Database.resources.Users;

import java.util.Iterator;
import com.google.gson.Gson;


public class Test {

	public static void main(String[] args) {

		DatabaseConnector db = new DatabaseConnector();
		AsyncDocumentClient client = db.getDocumentClient();

		String UsersCollection = db.getCollectionString("Users");

		//db.createNewDBEntry("Users", "/name", client);
		Users user = new Users();
		user.setName("Test");
		user.setId("2");

		//client.createDocument(db.getCollectionString("Users"), user, new RequestOptions(), true);

		//Inserting Works
		/*
		String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Users");
		client.createDocument(collectionLink, user, null, true)
        .toCompletable()
				.await();
		*/

		FeedOptions queryOptions = new FeedOptions();
		queryOptions.setEnableCrossPartitionQuery(true);
		queryOptions.setMaxDegreeOfParallelism(-1);

		Iterator<FeedResponse<Document>> it = client.queryDocuments(
				UsersCollection, "SELECT * FROM Users",
				queryOptions).toBlocking().getIterator();

		System.out.println( "Result:");
		while( it.hasNext())
			for( Document d : it.next().getResults())
				System.out.println( d.toJson());

		it = client.queryDocuments(
				UsersCollection, "SELECT * FROM Users u WHERE u.id = '2'",
				queryOptions).toBlocking().getIterator();

		System.out.println( "Result:");
		while( it.hasNext())
			for( Document d : it.next().getResults()) {
				System.out.println( d.toJson());
				Gson g = new Gson();
				Users u = g.fromJson(d.toJson(), Users.class);
				System.out.println( u.getId());
			}
		System.out.println("done");


	}
}