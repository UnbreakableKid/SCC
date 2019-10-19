package resources.Database;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.Database;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.PartitionKeyDefinition;
import com.microsoft.azure.cosmosdb.UniqueKey;
import com.microsoft.azure.cosmosdb.UniqueKeyPolicy;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

public class DatabaseConnector {
    
    private static ResourceBundle rb = ResourceBundle.getBundle("config");

 
   	private static AsyncDocumentClient client;

    public static synchronized AsyncDocumentClient getDocumentClient() {
        if (client == null) {
            ConnectionPolicy connectionPolicy = ConnectionPolicy.GetDefault();
            client = new AsyncDocumentClient.Builder().withServiceEndpoint(rb.getString("db.endpoint"))
                    .withMasterKeyOrResourceToken(rb.getString("db.masterkey")).withConnectionPolicy(connectionPolicy)
                    .withConsistencyLevel(ConsistencyLevel.Eventual).build();
        }
        return client;
    }

    static String getDatabaseString() {
		return String.format("/dbs/%s", rb.getString("db.database"));
	}
    
    public static String getCollectionString(String col) {
        return String.format("/dbs/%s/colls/%s", rb.getString("db.database"), col);
    }
    
    public void createDatabase(AsyncDocumentClient client) {

        Database databaseDefinition = new Database();
        databaseDefinition.setId(rb.getString("db.database"));
        client.createDatabase(databaseDefinition, null).toCompletable().await();
    }
    
    //Example, createNew("Users", "/name")
    public void createNewDBEntry(String collection, String path) {

        AsyncDocumentClient client = getDocumentClient();
        List<Database> databaseList = client
					.queryDatabases("SELECT * FROM root r WHERE r.id='" + rb.getString("db.database") + "'", null).toBlocking()
					.first().getResults();
        if (databaseList.size() == 0) {

            createDatabase(client);

        }
        
        String collectionName = collection;
		List<DocumentCollection> collectionList = client.queryCollections(getDatabaseString(),
					"SELECT * FROM root r WHERE r.id='" + collectionName + "'", null).toBlocking().first().getResults();

        if (collectionList.size() == 0) {

            String databaseLink = getDatabaseString();
					DocumentCollection collectionDefinition = new DocumentCollection();
					collectionDefinition.setId(collectionName);
					PartitionKeyDefinition partitionKeyDef = new PartitionKeyDefinition();
					partitionKeyDef.setPaths(Arrays.asList(path));
					collectionDefinition.setPartitionKey(partitionKeyDef);
					
					UniqueKeyPolicy uniqueKeyDef = new UniqueKeyPolicy();
					UniqueKey uniqueKey = new UniqueKey();
					uniqueKey.setPaths(Arrays.asList(path));
					uniqueKeyDef.setUniqueKeys(Arrays.asList(uniqueKey));
					collectionDefinition.setUniqueKeyPolicy(uniqueKeyDef);
					
					client.createCollection(databaseLink, collectionDefinition, null).toCompletable().await();
        }

        
    }


    
    
}