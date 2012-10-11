package fr.pierre.entretien.twittsearch.service;

import com.sun.jersey.api.client.ClientResponse;
import fr.pierre.entretien.twittsearch.model.Twittsearch;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;

import static org.fest.assertions.Assertions.assertThat;

public class TwittsearchServiceTest {


	public static final String JAVA = "JAVA";
	TwittsearchService service;

	@Before
	public void setUp(){
		service = new TwittsearchService();

	}

	@Test
	public void should_call_twitter_with_status_ok(){
		ClientResponse cr = service.callTwitterApiWithJerseyClient(JAVA);
		assertThat(cr.getStatus()).isEqualTo(Status.OK.getStatusCode());
	}

	@Test
	public void should_call_twitter_with_good_entity() throws UnsupportedEncodingException, JSONException {
		Twittsearch twittsearch = service.callTwitterAPI(JAVA);
		assertThat(twittsearch.getResults()).isNotEmpty();
		assertThat(twittsearch.getDateQuery()).isNotNull();
		assertThat(twittsearch.getQuery()).isEqualTo(JAVA);
	}


}
