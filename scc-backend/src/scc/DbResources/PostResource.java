package scc.DbResources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.PartitionKey;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;

import resources.Database.DatabaseConnector;
import resources.Database.entities.Communities;
import resources.Database.entities.Posts;
import resources.Database.entities.Users;

@Path("/post")
public class PostResource {

    private DatabaseConnector db = new DatabaseConnector();
    private AsyncDocumentClient client = db.getDocumentClient();

    private String UsersCollection = db.getCollectionString("Posts");
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Posts getPost(@PathParam("id") String id) {

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(UsersCollection,
                String.format("SELECT * FROM Posts p WHERE p.id = '%s'", id), queryOptions).toBlocking().getIterator();

        if (it.hasNext())
            for (Document d : it.next().getResults()) {
                System.out.println(d.toJson());
                Gson g = new Gson();
                Posts u = g.fromJson(d.toJson(), Posts.class);
                return u;
            }

        return null;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public List<Posts> getReplies(@PathParam("id") String id){

        List<Posts> z = new ArrayList<>();
        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        Iterator<FeedResponse<Document>> it = client.queryDocuments(
                UsersCollection, String.format("SELECT * FROM Posts p WHERE p.parentId = '%s'", id),
                queryOptions).toBlocking().getIterator();

       
        while (it.hasNext()) {
            for (Document d : it.next().getResults()) {
                System.out.println("Reply:");
                System.out.println(d.toJson());
                Gson g = new Gson();
                Posts u = g.fromJson(d.toJson(), Posts.class);
                z.add(u);
            }
        }

        return z;

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public String addPost(Posts post) {

        UserResource ur = new UserResource();
        CommunityResource cr = new CommunityResource();
        Users u = ur.getUser(post.getCreator().getId());
        Communities c = cr.getCommunity(post.getCommunity().getId());
        
        if (u != null && c != null && post.getLikes().size() == 0) {

            String collectionLink = String.format("/dbs/%s/colls/%s", "SCC-56982", "Posts");
            ResourceResponse<Document> resourceResponse = client.createDocument(collectionLink, post, null, false)
                    .toBlocking().last();
            Document document = resourceResponse.getResource();
            return document.get("id").toString();
        }
        return null;
    }
    
    @PUT
    @Path("/{postid}/like/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String likePost(@PathParam("postid") String id, @PathParam("userid") String userid) {

        UserResource ur = new UserResource();
        System.out.println("DOING GET POST");

        Posts p = getPost(id);
        
        RequestOptions options = new RequestOptions();
        options.setPartitionKey(new PartitionKey(p.getTitle()));

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        String document = String.format("/dbs/%s/colls/%s/docs/%s", "SCC-56982", "Posts", id);
        System.out.println("DOING DELETE");
        client.deleteDocument(document, options).toCompletable().await();
        
        System.out.println(document);
        System.out.println(getPost(id));

        
        List<Users> likes = p.getLikes();
        System.out.println("DOING GET USER");

        Users u = ur.getUser(userid);
        likes.add(u);

        p.setLikes(likes);

        System.out.println("DOING ADD POST");
        String result = addPost(p);


        return result;
    }
    @PUT
    @Path("/{postid}/unlike/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String unlikePost(@PathParam("postid") String id, @PathParam("userid") String userid) {

        UserResource ur = new UserResource();
        System.out.println("DOING GET POST");

        Posts p = getPost(id);
        
        RequestOptions options = new RequestOptions();
        options.setPartitionKey(new PartitionKey(p.getTitle()));

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setEnableCrossPartitionQuery(true);
        queryOptions.setMaxDegreeOfParallelism(-1);

        String document = String.format("/dbs/%s/colls/%s/docs/%s", "SCC-56982", "Posts", id);
        System.out.println("DOING DELETE");
        client.deleteDocument(document, options).toCompletable().await();
        
        System.out.println(document);
        System.out.println(getPost(id));

        
        List<Users> likes = p.getLikes();
        System.out.println("DOING GET USER");

        Users u = ur.getUser(userid);
        
        System.out.println(likes);
        
        for (int i = 0; i < likes.size(); i++) {
            Users c = likes.get(i);
            System.out.println(c.getId());
            System.out.println(u.getId());
            if (c.getId().equals(u.getId()))
                likes.remove(i);
        }
        
        System.out.println(likes);
        p.setLikes(likes);


        System.out.println("DOING ADD POST");
        String result = addPost(p);


        return result;
    }
    

}
