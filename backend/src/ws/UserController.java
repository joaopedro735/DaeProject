package ws;

import dtos.EmailDTO;
import ejbs.EmailBean;
import ejbs.UserBean;
import entities.User;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserController {

    @EJB
    private UserBean userBean;

    @EJB
    private EmailBean emailBean;

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MyEntityNotFoundException, MessagingException {

        try {
            User user = userBean.find(username);
            if (user == null) {
                throw new MyEntityNotFoundException("User with username '" + username + "' not found in our records.");
            }
            emailBean.send(user.getEmail(), email.getSubject(), email.getMessage());
            return Response.status(Response.Status.OK).entity("E-mail sent").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error sending email").build();
        }
    }
}
