package se.johsjo.web.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.johsjo.web.model.WorkItem;
import se.johsjo.web.resource.filter.AuthorizationRequestFilter;

public class WorkItemResourceTest {
	
	public static final String URI = "http://127.0.0.1:8080";
	
	private final String token = "Auth-token";
	private final String tokenPassword = "dummy";

	ObjectMapper mapper = new ObjectMapper();
	
	WebTarget webTarget;

	@Before
	public void setUp() {
		webTarget = ClientBuilder.newClient(new ClientConfig().register(AuthorizationRequestFilter.class))
								 .target(URI);
	}
	
	@Test
	public final void canAddWorkItem() {
		String jsonWorkItem = "{"+
			    "\"id\": -1,"+
			    "\"createdDate\": \"2017-04-01\","+
			    "\"createdBy\": \"DreamierTeam\","+
			    "\"lastModifiedDate\": \"2017-04-01\","+
			    "\"lastModifiedBy\": \"DreamierTeam\","+
			    "\"title\": \"title1225\","+
			    "\"description\": \"descrdddiptions12\","+
			    "\"status\": \"UNSTARTED\","+
			    "\"user\": null,"+
			    "\"dateOfCompletion\": null"+
			  "}";
		
		Response response = webTarget.path("/workitems")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .post(Entity.entity(jsonWorkItem, MediaType.APPLICATION_JSON));
		
		assertThat(response.getStatus(), is(201));
	}
	
	@Test
	public final void canAddIssueToWorkItem() {
		long workItemId = 6L;
		
		Response response = webTarget.path("/workitems/"+ workItemId + "/issues")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .post(Entity.entity("HÄR ÄR ETT FEL", MediaType.APPLICATION_JSON));
		
		assertThat(response.getStatus(), is(201));
	}
	
	@Test
	public final void canGetWorkItem() {
		Long workItemId = 1L;
		
		Response response = webTarget.path("/workitems/" + workItemId)
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
	
		WorkItem workItem = response.readEntity(WorkItem.class);
		
		assertThat(response.getStatus(), is(200));
		assertThat(workItem.getId(), is(workItemId));
	}
	
	@Test
	public final void canGetWorkItemsWithIssues() {
		Response response = webTarget.path("workitems/issues")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		assertThat(response.getStatus(), is(200));
	}
	
	@Test
	public final void canUpdateWorkItem() throws JsonProcessingException {
		
		long workItemId = 2L;
		
		Response response = webTarget.path("/workitems/"+ workItemId)
				 .request(MediaType.APPLICATION_JSON_TYPE)
				 .header(token, tokenPassword)
				 .get();
		
		WorkItem workitemFromBD = response.readEntity(WorkItem.class);
		workitemFromBD.setDescription("En Uppdatering");
		
		Response responseUpdate = webTarget.path("/workitems/"+ workItemId)
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .put(Entity.entity(mapper.writeValueAsString(workitemFromBD), MediaType.APPLICATION_JSON));
		
		assertThat(responseUpdate.getStatus(), is(200));
		
	}
	
	@Test
	public final void canUpdateIssue() {
		long workItemId = 2L;
		long issueId = 94L;
		
		String jsonIssue =   "{"+
			    "\"id\": " + issueId + ","+
			    "\"description\": \"nåt nytt\""+
			  "}";
		
		Response responseUpdate = webTarget.path("/workitems/"+ workItemId + "/issues/" + issueId)
										   .request(MediaType.APPLICATION_JSON_TYPE)
										   .header(token, tokenPassword)
										   .put(Entity.entity(jsonIssue, MediaType.APPLICATION_JSON));
		
		assertThat(responseUpdate.getStatus(), is(200));
	}
}
