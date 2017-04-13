package se.johsjo.web.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.johsjo.web.resource.TeamResource;
import se.johsjo.web.resource.UserResource;
import se.johsjo.web.resource.WorkItemResource;
import se.johsjo.web.resource.exception.NumberFormatExceptionMapper;
import se.johsjo.web.resource.filter.AuthorizationRequestFilter;


@Component
public class JerseyConfig extends ResourceConfig{
	
	public JerseyConfig() {
		register(UserResource.class);
		register(TeamResource.class);
		register(WorkItemResource.class);
		
		register(NumberFormatExceptionMapper.class);
		register(AuthorizationRequestFilter.class);
	}

}
