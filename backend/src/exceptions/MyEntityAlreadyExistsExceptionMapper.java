package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyEntityAlreadyExistsExceptionMapper implements ExceptionMapper<MyEntityAlreadyExistsException> {
    @Override
    public Response toResponse(MyEntityAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
