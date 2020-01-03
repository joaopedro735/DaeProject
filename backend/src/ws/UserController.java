package ws;

import dtos.AdministratorDTO;
import dtos.EmailDTO;
import dtos.UserDTO;
import ejbs.EmailBean;
import ejbs.UserBean;
import entities.Administrator;
import entities.User;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserController {

    @EJB
    private UserBean userBean;

    @EJB
    private EmailBean emailBean;

    private SecurityContext securityContext;

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getBirthday().toString()
        );
    }

    public static List<UserDTO> toDTOs(List<User> users) {
        return users.stream().map(UserController::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public Response getUsers() throws MyEntityNotFoundException, MessagingException {
        String msg;
        try {
            System.out.println("DEBUG1");
            GenericEntity<List<UserDTO>> entity = new GenericEntity<List<UserDTO>>(toDTOs(userBean.all())) {
            };
            System.out.println("DEBUG2");
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_USERS --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getAdministratorBySearch(@PathParam("tosearch") String toSearch){
        GenericEntity<List<UserDTO>> entity = new GenericEntity<List<UserDTO>>(toDTOs(userBean.findBySearch(toSearch))) {
        };
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @GET
    @Path("/types")
    public Response getTypesUsers() throws MyEntityNotFoundException, MessagingException {
        try {

            return Response.status(Response.Status.OK).entity("E-mail sent").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error sending email").build();
        }
    }


    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
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
        return Response.status(Response.Status.FORBIDDEN).build();
    }


}
