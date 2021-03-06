package ws;

import dtos.TrainerDTO;
import dtos.TypeDTO;
import ejbs.TrainerBean;
import ejbs.TypeBean;
import entities.Trainer;
import entities.Type;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/types")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed("Administrator")
public class TypeController {
    @EJB
    private TypeBean typeBean;

    @Context
    private SecurityContext securityContext;

    public static TypeDTO toDTO(Type type) {
        return new TypeDTO(
                (type.getId()),
                type.getName()
        );
    }

    public static List<TypeDTO> toDTOs(Collection<Type> types) {
        return types.stream().map(TypeController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        String msg;
        try {
            GenericEntity<List<TypeDTO>> entity = new GenericEntity<List<TypeDTO>>(toDTOs(typeBean.all())) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_TYPES --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

/*
    @GET
    @Path("/{id}")
    public Response getTrainerDetails(@PathParam("id") String username) {
        Principal principal = securityContext.getUserPrincipal();
        System.out.println(principal.getName());
        if (securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            Trainer trainer = trainerBean.find(username);
            return Response.status(Response.Status.OK).entity(toDTO(trainer)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getTrainersBySearch(@PathParam("tosearch") String toSearch){
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            GenericEntity<List<TrainerDTO>> entity = new GenericEntity<List<TrainerDTO>>(toDTOs(trainerBean.findBySearch(toSearch))) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
*/
    @POST
    @Path("/")
    public Response createNewType(TypeDTO typeDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        Type type = typeBean.create(typeDTO.getName());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(type))
                .build();

    }

    @PUT
    @Path("/{id}")
    public Response updateType(@PathParam("id") int id, TypeDTO typeDTO){
        Type type;
        String msg;
        try {
            typeBean.update(id, typeDTO.getName());

            return Response.status(Response.Status.OK).build();
        } catch (Exception e){
            msg = "ERROR_UPDATING_ADMINISTRATOR ---> " + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteAdministrator(@PathParam("id") int id) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            typeBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            msg = "ERROR_DELETING_TYPE --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

}
