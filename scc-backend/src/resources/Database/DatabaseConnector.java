package resources.Database;


import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

public class DatabaseConnector {
    
 
   	private static AsyncDocumentClient client;

    public synchronized AsyncDocumentClient getDocumentClient() {
        if (client == null) {
            ConnectionPolicy connectionPolicy = ConnectionPolicy.GetDefault();
            client = new AsyncDocumentClient.Builder().withServiceEndpoint("https://scc1920-56982.documents.azure.com:443/")
                    .withMasterKeyOrResourceToken("O7aHvn3kz9CkhVja4NmMFKl34nvXvsFfzYaJl9cUq00rU7rEMD7F3i70x7xG2joRq67Hi3axNJbzLbqDIr4C2g==").withConnectionPolicy(connectionPolicy)
                    .withConsistencyLevel(ConsistencyLevel.Session).build();
        }
        return client;
    }

    static String getDatabaseString() {
		return String.format("/dbs/%s","SCC-56982");
	}
    
    public String getCollectionString(String col) {
        return String.format("/dbs/%s/colls/%s","SCC-56982", col);
    }
    
}