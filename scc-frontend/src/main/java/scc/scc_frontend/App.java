package scc.scc_frontend;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.*;

/**
 * Hello world!
 *
 */
public class App
{
	public static void main(String[] args) {

		try {
			String hostname = "https://scc-backend-56982.azurewebsites.net/";
			if (args.length > 0)
				hostname = args[0];

			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);

			URI baseURI = UriBuilder.fromUri(hostname).build();

			WebTarget target = client.target(baseURI);

			byte[] image = target.path("/media/nmp155.jpg").request().accept(MediaType.APPLICATION_OCTET_STREAM)
					.get(byte[].class);

			System.err.println( image.length);

			Response res = target.path("/media").request().accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(image,MediaType.APPLICATION_OCTET_STREAM));

			System.err.println( res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


