package fr.pierre.entretien.twittsearch.resource;

import com.sun.jersey.spi.resource.Singleton;
import fr.pierre.entretien.twittsearch.model.Twittsearch;
import fr.pierre.entretien.twittsearch.service.TwittsearchService;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("twittsearch")
@Produces(APPLICATION_JSON + ";charset=utf-8")
@Component
@Singleton
public class TwittSearchResource {

	@Context
	UriInfo uriInfo;

	@Autowired
	TwittsearchService service;

	@GET
	public Response search(@QueryParam("q") String query) throws JSONException {
		if (query!=null){
			Twittsearch resultSearch = service.search(query);
			URI location = URI.create(uriInfo.getAbsolutePath() + "/" + resultSearch.getId());
			return Response.created(location).entity(resultSearch).build();
		}else{
			return Response.ok(new GenericEntity<List<Twittsearch>>(service.history()) {
			}).build();
		}
	}

	@GET
	 @Path("{id:[0-9]+}")
	 public Response getTwittsearch(@PathParam("id") Integer id) {
		final Twittsearch twittsearch = service.getTwittsearch(id);
		if (twittsearch!=null)
			return Response.ok(twittsearch).build();
		else
			return Response.noContent().build();
	}

	@DELETE
	@Path("{id:[0-9]+}")
	public Response deleteTwittsearch(@PathParam("id") Integer id) {
		service.delete(id);
		return Response.noContent().build();
	}
}
