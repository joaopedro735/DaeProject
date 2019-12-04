package ws;

import dtos.SportDTO;
import ejbs.SportBean;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/sports")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SportController {
    @EJB
    private SportBean sportBean;

    @Context
    private SecurityContext securityContext;

    public static SportDTO toDTO(Sport sport, Function<SportDTO, SportDTO> fn) {
        SportDTO dto = new SportDTO(
                sport.getCode(),
                sport.getName());

        if (fn != null) {
            return fn.apply(dto);
        }
        return dto;
    }

    public static List<SportDTO> toDTOs(Collection<Sport> sports, Function<SportDTO, SportDTO> fn) {
        return sports.stream().map(s -> SportController.toDTO(s, fn)).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public Response all() {
        String msg;
        try {
            GenericEntity<List<SportDTO>> entity = new GenericEntity<List<SportDTO>>(toDTOs(sportBean.all(), null)) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_SPORTS --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSport(SportDTO sportDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Sport newSport = sportBean.create(sportDTO.getName());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newSport, null))
                .build();

    }

    @PUT
    @Path("/{code}")
    public Response updateSport(@PathParam("code") Integer code, SportDTO sportDTO) throws MyEntityNotFoundException {
        Sport sport = sportBean.update(code, sportDTO.getName());
        return Response.status(Response.Status.ACCEPTED).entity(toDTO(sport, null)).build();
    }

    @GET
    @Path("/{code}")
    public Response getSportDetails(@PathParam("code") Integer code) {
        try {
            Sport sport = sportBean.find(code);
            return Response.status(Response.Status.OK)
                    .entity(toDTO(sport, /*dto -> {
                            dto.setTrainers(TrainerController.toDTOs(sport.getTrainers()));
                            return dto;
                }*/null)).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_SPORT", e);
        }
    }

    @PUT
    @Path("/{code}/trainers/{username}/enroll")
    public Response enrollTrainer(@PathParam("code") Integer code, @PathParam("username") String username) {
        try {
            sportBean.enrollAthlete(username, code);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_TRAINER", e);
        }
    }

    @PUT
    @Path("/{code}/trainers/{username}/unroll")
    public Response unrollTrainer(@PathParam("code") Integer code, @PathParam("username") String username) {
        try {
            sportBean.unrollTrainer(username, code);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_TRAINER", e);
        }
    }
}
