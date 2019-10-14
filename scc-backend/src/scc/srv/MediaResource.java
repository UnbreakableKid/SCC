package scc.srv;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Path("/media")
public class MediaResource {

    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=scc56982;AccountKey=VXmXlnQOK01LACbV5NC4+7tVl84Rvth2MIPRyclUH3RC67pjQ2t1eLa+o89HsyRWwUwTQdJeLX6yVnoJSA/ERA==;EndpointSuffix=core.windows.net";
   

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public void upload(byte[] contents) {

        CloudBlobContainer container;
        try {
           
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            container = blobClient.getContainerReference("images");

            try {
                CloudBlob blob = container.getBlockBlobReference("hi.jpg");
                blob.uploadFromByteArray(contents, 0, contents.length);

            } catch (URISyntaxException | StorageException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (URISyntaxException | StorageException | InvalidKeyException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @GET
    public void download(String name) {

        CloudBlobContainer container;
        try {

            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            container = blobClient.getContainerReference("images");

            CloudBlob blob = container.getBlobReferenceFromServer(name);

            blob.download(new FileOutputStream("C:/Test" + blob.getName()));

        } catch (URISyntaxException | StorageException | IOException | InvalidKeyException e1) {
            e1.printStackTrace();

        }
    }
}
