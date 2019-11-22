package ws;

import dtos.TrainerDTO;
import ejbs.TrainerBean;
import entities.Trainer;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
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
                trainer.getEmail()
        );
    }

    public static List<TrainerDTO> toDTOs(List<Trainer> trainers) {
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
}
