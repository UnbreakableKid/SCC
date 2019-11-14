package main.java.frontend;

import java.util.Iterator;
import java.util.UUID;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import main.java.frontend.Database.DatabaseConnector;
import main.java.frontend.Database.resources.Users;



public class Test {

	public static void main(String[] args) throws Exception {

		DatabaseConnector db = new DatabaseConnector();
		AsyncDocumentClient client = db.getDocumentClient();

		String UsersCollection = db.getCollectionString("Users");

		//db.createNewDBEntry("Users", "/name", client);
		Users user = new Users();
		user.setName("Test");
        UUID id = UUID.randomUUID();
		user.setId(id);


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
				UsersCollection, "SELECT * FROM Users u WHERE u.name = 'Test'",
				queryOptions).toBlocking().getIterator();

		System.out.println( "Result:");
		if( it.hasNext())
			for (Document d : it.next().getResults()) {
				Gson g = new Gson();
				Users u = g.fromJson(d.toJson(), Users.class);
				System.out.println("He has id:" + u.getId());
			}
		else
			throw new Exception("doesn't exist");
		System.out.println("done");


	}
}