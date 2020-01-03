package ws;

import dtos.AdministratorDTO;
import ejbs.AdministratorBean;
import ejbs.UserBean;
import entities.Administrator;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorController {
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;

    public static AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getName(),
                administrator.getEmail(),
                administrator.getBirthday().toString()
        );
    }

    public static List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(AdministratorController::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public Response all() {
        String msg;
        try {
            GenericEntity<List<AdministratorDTO>> entity = new GenericEntity<List<AdministratorDTO>>(toDTOs(administratorBean.all())) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_ADMINISTRATORS --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("/{username}")
    public Response getAdministratorDetails(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        System.out.println(principal.getName());
        if (securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            Administrator administrator = administratorBean.find(username);
            return Response.status(Response.Status.OK).entity(toDTO(administrator)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getAdministratorBySearch(@PathParam("tosearch") String toSearch){
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            GenericEntity<List<AdministratorDTO>> entity = new GenericEntity<List<AdministratorDTO>>(toDTOs(administratorBean.findBySearch(toSearch))) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/")
    public Response createNewAdministrator(AdministratorDTO administratorDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Administrator")) {
            Administrator newAdministrator = administratorBean.create(administratorDTO.getUsername(),
                    administratorDTO.getPassword(),
                    administratorDTO.getName(),
                    administratorDTO.getEmail(),
                    administratorDTO.getBirthday());

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(newAdministrator))
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("/{username}")
    public Response updateAdministrator(@PathParam("username") String username, AdministratorDTO administratorDTO){
        String msg;
        User user;
        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            try {
                user = administratorBean.find(username);
                if (user == null){
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                administratorBean.update(username, administratorDTO.getPassword(), administratorDTO.getName(), administratorDTO.getEmail(), administratorDTO.getBirthday());

                return Response.status(Response.Status.OK).build();
            } catch (Exception e){
                msg = "ERROR_UPDATING_ADMINISTRATOR ---> " + e.getMessage();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteAdministrator(@PathParam("username") String username) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Administrator")) {
            try {
                userBean.remove(username);
                return Response.status(Response.Status.OK).build();
            } catch (Exception e) {
                msg = "ERROR_DELETING_ADMINISTRATOR --->" + e.getMessage();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
