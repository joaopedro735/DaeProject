package ws;

import dtos.AdministratorDTO;
import dtos.TrainerDTO;
import ejbs.TrainerBean;
import entities.Administrator;
import entities.Trainer;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/trainers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TrainerController {
    @EJB
    private TrainerBean trainerBean;

    @Context
    private SecurityContext securityContext;

    public static TrainerDTO toDTO(Trainer trainer) {
        return new TrainerDTO(
                trainer.getUsername(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getBirthday().toString()
        );
    }

    public static List<TrainerDTO> toDTOs(Collection<Trainer> trainers) {
        return trainers.stream().map(TrainerController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        String msg;
        try {
            GenericEntity<List<TrainerDTO>> entity = new GenericEntity<List<TrainerDTO>>(toDTOs(trainerBean.all())) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_TRAINERS --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }


    @GET
    @Path("/{username}")
    public Response getTrainerDetails(@PathParam("username") String username) {
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

    @POST
    @Path("/")
    public Response createNewTrainer(TrainerDTO trainerDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        Trainer trainer = trainerBean.create(trainerDTO.getUsername(),
                trainerDTO.getPassword(),
                trainerDTO.getName(),
                trainerDTO.getEmail(),
                trainerDTO.getBirthday());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(trainer))
                .build();

    }

    @PUT
    @Path("/{username}")
    public Response updateAdministrator(@PathParam("username") String username, TrainerDTO trainerDTO){
        String msg;
        User user;
        System.out.println(username);
        try {
            user = trainerBean.find(username);
            if (user == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            trainerBean.update(username, trainerDTO.getPassword(), trainerDTO.getName(), trainerDTO.getEmail(), trainerDTO.getBirthday());

            return Response.status(Response.Status.OK).build();
        } catch (Exception e){
            msg = "ERROR_UPDATING_ADMINISTRATOR ---> " + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteAdministrator(@PathParam("username") String username) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            trainerBean.remove(username);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            msg = "ERROR_DELETING_ADMINISTRATOR --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
