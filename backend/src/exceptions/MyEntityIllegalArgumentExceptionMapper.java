package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyEntityIllegalArgumentExceptionMapper implements ExceptionMapper<MyEntityIllegalArgumentException> {
    @Override
    public Response toResponse(MyEntityIllegalArgumentException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
