package se.johsjo.web.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

public class NotFoundException extends WebApplicationException{

	private static final long serialVersionUID = -6645517099931028293L;
	
	public NotFoundException() {
		super(Status.NOT_FOUND);
	}

}
