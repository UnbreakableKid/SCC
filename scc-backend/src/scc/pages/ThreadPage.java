package scc.pages;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import org.json.JSONObject;

import scc.DbResources.PostResource;

@Path("pages/thread/{id}")
public class ThreadPage {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getThread(@PathParam("id") String id) {
        
        PostResource pr = new PostResource();
        resources.Database.entities.Posts p = pr.getPost(id);
        
        if (p != null) {
            Gson gson = new Gson();
            List<resources.Database.entities.Posts> list = pr.getReplies(id);
            List<JSONObject> ar = new ArrayList<>();

            JSONObject json = new JSONObject();

            json.put("Initial post", gson.toJson(p));

            for (int i = 0; i < list.size(); i++) {
                JSONObject reply = replyHelper(list.get(i).getId());
                ar.add(i, reply);
            }

            json.put("Replies", ar);

            String result = json.toString();

            return result;
        } else
            return null;
    }

    public JSONObject replyHelper(@PathParam("id") String id) {

        PostResource pr = new PostResource();
        resources.Database.entities.Posts p = pr.getPost(id);
        Gson gson = new Gson();

        if (p != null) {
            List<resources.Database.entities.Posts> list = pr.getReplies(id);
            List<JSONObject> ar = new ArrayList<>();

            JSONObject json = new JSONObject();
         

            json.put("Reply", gson.toJson(p));

            for (int i = 0; i < list.size(); i++) {
                JSONObject reply = replyHelper(list.get(i).getId());
                ar.add(i, reply);
            }

            json.put("Replies", ar);

            return json;
        } else
            return null;
    }
}