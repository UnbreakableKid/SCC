package resources.Database;

import java.util.ResourceBundle;

import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

public class DatabaseConnector {
    
    private static ResourceBundle rb = ResourceBundle.getBundle("resources.config");

 
   	private static AsyncDocumentClient client;

    public synchronized AsyncDocumentClient getDocumentClient() {
        if (client == null) {
            ConnectionPolicy connectionPolicy = ConnectionPolicy.GetDefault();
            client = new AsyncDocumentClient.Builder().withServiceEndpoint(rb.getString("db.endpoint"))
                    .withMasterKeyOrResourceToken(rb.getString("db.masterkey")).withConnectionPolicy(connectionPolicy)
                    .withConsistencyLevel(ConsistencyLevel.Session).build();
        }
        return client;
    }

    static String getDatabaseString() {
		return String.format("/dbs/%s", rb.getString("db.database"));
	}
    
    public String getCollectionString(String col) {
        return String.format("/dbs/%s/colls/%s", rb.getString("db.database"), col);
    }
    
}