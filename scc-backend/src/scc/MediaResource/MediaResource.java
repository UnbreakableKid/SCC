package scc.MediaResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

@Path("/media")
public class MediaResource {

    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=scc56982;AccountKey=VXmXlnQOK01LACbV5NC4+7tVl84Rvth2MIPRyclUH3RC67pjQ2t1eLa+o89HsyRWwUwTQdJeLX6yVnoJSA/ERA==;EndpointSuffix=core.windows.net";
   

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public void upload(@PathParam("id") String id,byte[] contents) {

        CloudBlobContainer container;
        try {
           
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            container = blobClient.getContainerReference("images");

            try {
                CloudBlob blob = container.getBlockBlobReference(id);
                blob.uploadFromByteArray(contents, 0, contents.length);

            } catch (URISyntaxException | StorageException | IOException e) {
                e.printStackTrace();
            }

        } catch (URISyntaxException | StorageException | InvalidKeyException e1) {
            e1.printStackTrace();
        }
    }


    @GET
    @Path("/{name}")
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public byte[] download(@PathParam("name") String name) {

        byte[] contents = null;
        CloudBlobContainer container;
        try {

            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            container = blobClient.getContainerReference("images");

            CloudBlob blob = container.getBlobReferenceFromServer(name);
 
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            blob.download(out);
            
            out.close();

            contents = out.toByteArray();


        } catch (URISyntaxException | StorageException | IOException | InvalidKeyException e1) {
            e1.printStackTrace();

        }
        
        return contents;
    }
}
