package se.johsjo.web.resource;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Component;

import se.johsjo.web.data.TeamUserContainer;
import se.johsjo.web.model.Team;
import se.johsjo.web.model.User;
import se.johsjo.web.model.WorkItem;
import se.johsjo.web.resource.filter.AuthToken;
import se.johsjo.web.service.ServiceException;
import se.johsjo.web.service.TeamService;
import se.johsjo.web.service.UserService;
import se.johsjo.web.service.WorkItemService;

@AuthToken
@Component
@Path("/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class TeamResource {

	private final TeamService teamService;
	private final UserService userService;
	private final WorkItemService workItemService;

	@Context
	private UriInfo uriInfo;

	public TeamResource(TeamService teamService, UserService userService, WorkItemService workItemService) {
		this.teamService = teamService;
		this.userService = userService;
		this.workItemService = workItemService;
	}

	@POST
	public Response addTeam(Team team) throws ServiceException {
		Team newTeam = teamService.getTeamById(team.getId());
		if (newTeam == null) {
			team = new Team(team.getTeamName());
			teamService.addOrUpdateTeam(team);
			URI location = uriInfo.getAbsolutePathBuilder().path(team.getId().toString()).build();
			return Response.created(location).build();

		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	public Response getAllTeams() {
		Collection<Team> teams = teamService.getAllTeams();
		if (teams.isEmpty()) {
			return Response.noContent().build();
		}
		return Response.ok(teams).build();
	}

	@GET
	@Path("{id}")
	public Response getTeamById(@PathParam("id") String id) {
		Long longId = Long.parseLong(id);
		Team team = teamService.getTeamById(longId);
		if (team == null) {
			throw new NotFoundException();
		}
		return Response.ok(team).build();
	}

	@GET
	@Path("{id}/users")
	public Response getUsersFromTeam(@PathParam("id") String id) {
		Long longId = Long.parseLong(id);
		Collection<User> users = userService.getUsersByTeamId(longId);
		if (users.isEmpty()) {
			return Response.noContent().build();
		}
		return Response.ok(users).build();
	}

	@GET
	@Path("{id}/workitems")
	public Response getWorkItemsFromTeam(@PathParam("id") String id) {
		Long longId = Long.parseLong(id);
		Collection<WorkItem> workItems = workItemService.getAllWorkItemsByTeam(longId);
		if (workItems.isEmpty()) {
			return Response.noContent().build();
		}
		return Response.ok(workItems).build();
	}

	@PUT
	@Path("{id}")
	public Response updateTeam(Team team, @PathParam("id") String id) throws ServiceException {
		Long longId = Long.parseLong(id);
		if (team.getId() != longId) {
			throw new WebApplicationException("conflicting id's", Status.BAD_REQUEST);
		}
		Team teamFromDb = teamService.getTeamById(team.getId());
		if (teamFromDb != null) {
			teamService.addOrUpdateTeam(team);
			return Response.ok().build();
		}
		throw new NotFoundException();
	}

	@PUT
	@Path("{id}/users/{userId}")
	public Response addUserToTeam(@PathParam("id") String id, @PathParam("userId") String userId,
			TeamUserContainer container) throws WebApplicationException {
		
		Long longId = Long.parseLong(id);

		Team team = container.getTeam();
		User user = container.getUser();

		if (team.getId() != longId && user.getUserId() != userId) {
			throw new WebApplicationException("conflicting id's", Status.BAD_REQUEST);
		}
		try {
			teamService.addUserToTeam(user, team);
		} catch (ServiceException e) {
			return Response.status(Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

}
