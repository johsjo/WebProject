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

import se.johsjo.web.model.User;
import se.johsjo.web.resource.filter.AuthorizationRequestFilter;

public class UserResourceTest {
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
	public final void canAddUser() {
		String jsonUser = "{"+
			    "\"id\": -1,"+
			    "\"username\": \"usedrddn3amhyadsddd1df2d\","+
			    "\"firstname\": \"firstname\","+
			    "\"lastname\": \"lastnam\","+
			    "\"userId\": \"20543\","+
			    "\"activeUser\": true"+
			  "}";
		
		Response response = webTarget.path("/users").request(MediaType.APPLICATION_JSON_TYPE)
													.header(token, tokenPassword)
													.post(Entity.entity(jsonUser, MediaType.APPLICATION_JSON));
		
		assertThat(response.getStatus(), is(201));
		
		String newTeamIdFromDB = (String) response.getHeaders()
												  .get("location")
												  .get(0);
		
		String[] stringArray = newTeamIdFromDB.split("/");
		String userIdFromResponse = stringArray[stringArray.length - 1];
		
		Response response2 = webTarget.path("/users/" + userIdFromResponse)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .header(token, tokenPassword)
									  .get();
		
		User userFromDB = response2.readEntity(User.class);
		
		assertThat(response2.getStatus(), is(200));
		assertNotNull(userFromDB);
		assertThat(userFromDB.getUserId(), is(userIdFromResponse));
	}
	
	@Test
	public final void canGetUserById() {
		String userId = "2007";
		
		Response response = webTarget.path("/users/" + userId)
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		User userFromDB = response.readEntity(User.class);
		
		assertThat(response.getStatus(), is(200));
		assertThat(userFromDB.getUserId(), is(userId));
	}
	
	//TODO NOT DONE //DOSE NOT WORK :-(
//http://127.0.0.1:8080/users?firstname=Johan&lastname=Sjölander&username=johsjo1234
//	getUserByName(@BeanParam UserQueryNameParam param)
	@Test
	public final void canGetUserByName() {
		
		Response response = webTarget.path("/users?firstname=Johan&lastname=Sjölander&username=johsjo1234")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		assertThat(response.getStatus(), is(200));
		
		User userFromDB = response.readEntity(User.class);
		
		assertThat(userFromDB.getFirstname(), is("Johan"));
		assertThat(userFromDB.getLastname(), is("Sjolander"));
		assertThat(userFromDB.getUserId(), is("johsjo1234"));
	}
	
	@Test
	public final void canGetWorkItemsByUser() {
		String userId = "2053";
		
		Response response = webTarget.path("/users/" + userId + "/workitems")
									 .request(MediaType.APPLICATION_JSON_TYPE)
									 .header(token, tokenPassword)
									 .get();
		
		assertThat(response.getStatus(), is(200));
	}
	
	@Test
	public final void canUppdateUser() throws JsonProcessingException {
		String jsonUser = "{"+
			    "\"id\": -1,"+
			    "\"username\": \"userUppdatTestd331s\","+
			    "\"firstname\": \"firstname\","+
			    "\"lastname\": \"lastnam\","+
			    "\"userId\": \"201145233\","+
			    "\"activeUser\": true"+
			  "}";
		
		Response response = webTarget.path("/users").request(MediaType.APPLICATION_JSON_TYPE)
													.header(token, tokenPassword)
													.post(Entity.entity(jsonUser, MediaType.APPLICATION_JSON));

		assertThat(response.getStatus(), is(201));
		
		String newTeamIdFromDB = (String) response.getHeaders()
												  .get("location")
												  .get(0);
		
		String[] stringArray = newTeamIdFromDB.split("/");
		String userIdFromResponse = stringArray[stringArray.length - 1];
		
		Response response2 = webTarget.path("/users/" + userIdFromResponse)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .header(token, tokenPassword)
									  .get();

		User userFromDB = response2.readEntity(User.class);
		
		userFromDB.setUsername("userUppdatTest1Updatednddn");
		
		Response responseUp = webTarget.path("/users/" + userFromDB.getUserId())
									   .request(MediaType.APPLICATION_JSON_TYPE)
									   .header(token, tokenPassword)
									   .put(Entity.entity(mapper.writeValueAsString(userFromDB), MediaType.APPLICATION_JSON));

		assertThat(responseUp.getStatus(), is(200));
		
	}
}
