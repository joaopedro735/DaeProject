package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenException implements ExceptionMapper<javax.ws.rs.ForbiddenException> {
    @Override
    public Response toResponse(javax.ws.rs.ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN).entity("UNAUTHORIZED").build();
    }
}
