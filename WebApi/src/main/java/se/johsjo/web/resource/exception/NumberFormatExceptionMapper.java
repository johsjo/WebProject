package se.johsjo.web.resource.exception;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

@Provider
public class NumberFormatExceptionMapper implements ExceptionMapper<NumberFormatException> {

	@Override
	public Response toResponse(NumberFormatException exception) {

		return Response.status(Status.BAD_REQUEST).build();

	}

}
