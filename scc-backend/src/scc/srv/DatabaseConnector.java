package scc.srv;

import java.util.ResourceBundle;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

public class DatabaseConnector {
    
    private static ResourceBundle rb = ResourceBundle.getBundle("config");

 
   	private static AsyncDocumentClient client;

    static synchronized AsyncDocumentClient getDocumentClient() {
        if (client == null) {
            ConnectionPolicy connectionPolicy = ConnectionPolicy.GetDefault();
            client = new AsyncDocumentClient.Builder().withServiceEndpoint(rb.getString("db.endpoint"))
                    .withMasterKeyOrResourceToken(rb.getString("db.masterkey")).withConnectionPolicy(connectionPolicy)
                    .withConsistencyLevel(ConsistencyLevel.Eventual).build();
        }
        return client;
    }
    
    
}