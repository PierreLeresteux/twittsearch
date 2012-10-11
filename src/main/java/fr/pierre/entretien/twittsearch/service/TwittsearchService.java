package fr.pierre.entretien.twittsearch.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import fr.pierre.entretien.twittsearch.model.Result;
import fr.pierre.entretien.twittsearch.model.Twittsearch;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.fest.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@Transactional(propagation = REQUIRED, readOnly = true, value = "transactionManager")
public class TwittsearchService {

	public static final String HTTP_SEARCH_TWITTER_COM = "http://search.twitter.com";
	public static final String SEARCH = "search";
	public static final String SEARCH_KEY = "q";
	public static final String HASHTAG_DECODE = "#";
	public static final String RESULT_TYPE = "result_type";
	public static final String RECENT = "recent";

	@PersistenceContext(name = "twittsearchPersistenceUnit")
	protected EntityManager entityManager;

	@VisibleForTesting
	public TwittsearchService() {
	}

	@Transactional(propagation = REQUIRES_NEW, readOnly = false, value = "transactionManager")
	public Twittsearch search(final String query) throws JSONException {
		Twittsearch twittsearch = callTwitterAPI(query);
		return saveSearch(twittsearch);
	}

	Twittsearch callTwitterAPI(final String query) throws JSONException {
		ClientResponse cr = callTwitterApiWithJerseyClient(query);
		final JSONObject jso = cr.getEntity(JSONObject.class);

		Twittsearch twittsearch = new Twittsearch(query);
		final JSONArray results = (JSONArray) jso.get("results");
		for (int i = 0; i < results.length(); i++) {
			final JSONObject result = (JSONObject) results.get(i);
			Result r = new Result();
			r.setUser(result.getString("from_user"));
			r.setText(result.getString("text"));
			r.setTwittsearch(twittsearch);
			twittsearch.getResults().add(r);
		}
		return twittsearch;
	}

	Twittsearch saveSearch(final Twittsearch twittsearch) throws JSONException {
		entityManager.persist(twittsearch);
		return twittsearch;
	}

	ClientResponse callTwitterApiWithJerseyClient(final String query) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource r = client.resource(HTTP_SEARCH_TWITTER_COM);
		return r.path(SEARCH).queryParam(SEARCH_KEY, HASHTAG_DECODE + query).queryParam(RESULT_TYPE, RECENT)
				.accept(APPLICATION_JSON).get(ClientResponse.class);
	}

	public List<Twittsearch> history() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Twittsearch> criteriaQuery = criteriaBuilder.createQuery(Twittsearch.class);
		Root<Twittsearch> root = criteriaQuery.from(Twittsearch.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

		final TypedQuery<Twittsearch> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Twittsearch getTwittsearch(final Integer id) {
		return entityManager.find(Twittsearch.class, id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "transactionManager")
	public void delete(final Integer id) {
		entityManager.remove(getTwittsearch(id));
	}
}
