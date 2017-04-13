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
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.johsjo.web.model.Team;
import se.johsjo.web.resource.filter.AuthorizationRequestFilter;

@RunWith(MockitoJUnitRunner.class)
public class TeamResourceTest {

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
	public final void canAddTeam() throws JsonProcessingException {
		String jsonTeam = "{" + "\"id\": -1,\"createdDate\": null," + "\"createdBy\": null,"
				+ "\"lastModifiedDate\": null," + "\"lastModifiedBy\": null,"
				+ "\"teamName\": \"JsersdeyTestdieng51d2345\"," + "\"activeTeam\": true" + "}";
		
		Response response = webTarget.path("/teams").request(MediaType.APPLICATION_JSON_TYPE)
													.header(token, tokenPassword)
													.post(Entity.entity(jsonTeam, MediaType.APPLICATION_JSON));
		assertThat(response.getStatus(), is(201));

		String newTeamIdFromDB = (String) response.getHeaders()
												  .get("location")
												  .get(0);
		
		String[] stringArray = newTeamIdFromDB.split("/");
		Long teamIdFromResponse = Long.parseLong(stringArray[stringArray.length - 1]);

		Response response2 = webTarget.path("/teams/" + teamIdFromResponse)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .header(token, tokenPassword)
									  .get();
		
		Team teamFromDB = response2.readEntity(Team.class);
		
		assertThat(response2.getStatus(), is(200));
		assertNotNull(teamFromDB);
		assertThat(teamFromDB.getId(), is(teamIdFromResponse));
	}

	@Test
	public final void canGetAllTeams() {
		Response response = webTarget.path("/teams")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header(token, tokenPassword)
				.get();
		
		assertThat(response.getStatus(), is(200));
	}
	
	@Test
	public final void canGetTeamById() {
		Long teamId = 7L;
		
		Response response = webTarget.path("/teams/" + teamId)
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		Team teamFromDB = response.readEntity(Team.class);
		
		assertThat(response.getStatus(), is(200));
		assertThat(teamFromDB.getId(), is(teamId));
	}

	@Test
	public final void canGetUsersFromTeam() {
		Long teamId = 7L;
		
		Response response =  webTarget.path("/teams/" + teamId + "/users")
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .header(token, tokenPassword)
									  .get();
		
		assertThat(response.getStatus(), is(200));
	}

	@Test
	public final void canGetWorkItemsFromTeam() {
		Long teamId = 7L;
		
		Response response = webTarget.path("/teams/" + teamId + "/workitems")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		assertThat(response.getStatus(), is(200));
	}

	@Test
	public final void canUpdateTeam() {
		String teamIdLocal;
		String teamJsonString = "{" + "\"id\": -1,\"createdDate\": null," + "\"createdBy\": null,"
				+ "\"lastModifiedDate\": null," + "\"lastModifiedBy\": null,"
				+ "\"teamName\": \"JserseyTestingPUTtest12345\"," + "\"activeTeam\": true" + "}";

		Response responseCr = webTarget.path("/teams")
									   .request(MediaType.APPLICATION_JSON_TYPE)
									   .header(token, tokenPassword)
									   .post(Entity.entity(teamJsonString, MediaType.APPLICATION_JSON));
		
		assertThat(responseCr.getStatus(), is(201));

		String testtest = (String) responseCr.getHeaders()
											 .get("location")
											 .get(0);
		
		String[] testArray = testtest.split("/");
		teamIdLocal = testArray[testArray.length - 1];

		String teamUpdateJsonString = "{" + "\"id\": " + teamIdLocal + ",\"createdDate\": null,"
				+ "\"createdBy\": null," + "\"lastModifiedDate\": null," + "\"lastModifiedBy\": null,"
				+ "\"teamName\": \"JserseyTestingUpdated" + teamIdLocal + "\"," + "\"activeTeam\": true" + "}";

		Response responseUp = webTarget.path("/teams/" + teamIdLocal)
									   .request(MediaType.APPLICATION_JSON_TYPE)
									   .header(token, tokenPassword)
									   .put(Entity.entity(teamUpdateJsonString, MediaType.APPLICATION_JSON));

		assertThat(responseUp.getStatus(), is(200));

	}

	@Test
	public final void canAddUserToTeam() {
		String teamUserContainer = "{" + "\"team\":{" + "\"id\": 7," + "\"createdDate\": \"2017-04-02\","
				+ "\"createdBy\": \"DreamierTeam\"," + "\"lastModifiedDate\": \"2017-04-01\","
				+ "\"lastModifiedBy\": \"DreamierTeam\"," + "\"teamName\": \"teamName3\"," + "\"activeTeam\": true"
				+ "}," + "\"user\":{" + "\"id\": 13," + "\"createdDate\": \"2017-04-01\","
				+ "\"createdBy\": \"DreamierTeam\"," + "\"lastModifiedDate\": \"2017-04-01\","
				+ "\"lastModifiedBy\": \"DreamierTeam\"," + "\"username\": \"userddnamhytdadsddd1\","
				+ "\"firstname\": \"firstname\"," + "\"lastname\": \"lastnam\"," + "\"userId\": \"2007\","
				+ "\"activeUser\": true" + "}" + "}";

		Response response = webTarget.path("teams/7/users/13").request(MediaType.APPLICATION_JSON_TYPE)
															  .header(token, tokenPassword)
															  .put(Entity.entity(teamUserContainer, MediaType.APPLICATION_JSON_TYPE));

		assertThat(response.getStatus(), is(200));
	}

}
