package scc.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

@Path("/search")
public class CognitiveSearch {
    String serviceName = "scc-56982-search";
    String queryKey = "D186CBF59419DCB96AA4AB3780E0F437";
    String hostname = "https://" + serviceName + ".search.windows.net/";
    ClientConfig config = new ClientConfig();
    Client client = ClientBuilder.newClient(config);
    URI baseURI = UriBuilder.fromUri(hostname).build();
    WebTarget target = client.target(baseURI);
    String index = "cosmosdb-index";

    @GET
    @Path("/{type}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSearch(@PathParam("type") String type, @PathParam("id") String id) throws IOException {
        
        URL url = new URL(String.format("https://%s.search.windows.net/indexes/%s/docs/search?api-version=2019-05-06", serviceName, index));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("api-key", queryKey);
        con.setRequestProperty("Accept", "application/json");


        String jsonInputString;

        String field = "";
        if (type.compareTo("creator") == 0) {
            field = "creator/name";
            jsonInputString = String.format("{\"queryType\":\"simple\", \"search\": \"%s\", \"searchFields\": \"%s\" ,\"count\": \"true\"}", id, field);

        } 
        else if (type.compareTo("msg") == 0) {
            field = "message";
            jsonInputString = String.format("{\"queryType\":\"simple\", \"search\": \"%s\", \"searchFields\": \"%s\" ,\"count\": \"true\"}", id, field);
        }else if (type.compareTo("community") == 0) {
            field = "community/name";
            jsonInputString = String.format(
                    "{\"queryType\":\"simple\", \"search\": \"s/%s\", \"searchFields\": \"%s\" ,\"count\": \"true\"}",
                    id, field);
            System.out.println(jsonInputString);
        }
        else {
            return null;
        }

            con.setDoOutput(true);
        
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

       try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
            }
            return response.toString();
}

    }
}