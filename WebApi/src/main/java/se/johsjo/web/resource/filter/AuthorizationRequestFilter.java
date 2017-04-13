package se.johsjo.web.resource.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

@AuthToken
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String token = null;
		if (requestContext.getHeaders().get("Auth-token") == null) {
			requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity("Enter Auth-token").build());
		} else {
			token = requestContext.getHeaders().get("Auth-token").get(0);
			if (!"dummy".equals(token)) {
				requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity("Wrong password").build());
			}
		}

	}
}
