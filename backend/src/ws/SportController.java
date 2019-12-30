package ws;

import dtos.SportDTO;
import dtos.TimeTableDTO;
import ejbs.AthleteBean;
import ejbs.SportBean;
import ejbs.SportRegistrationBean;
import ejbs.TimeTableBean;
import entities.Athlete;
import entities.Sport;
import entities.SportRegistration;
import entities.TimeTable;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/sports")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SportController {
    @EJB
    private SportBean sportBean;

    @EJB
    private TimeTableBean timeTableBean;

    @EJB
    private SportRegistrationBean sportRegistrationBean;

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
        return Response.status(Response.Status.ACCEPTED)
                .entity(toDTO(sport, null)).build();
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
                }*/dto -> {
                        dto.setTimeTables(TimeTableController.toDTOs(sport.getTimeTables(),null));
                        return dto;
                    })).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_SPORT", e);
        }
    }

    @PUT
    @Path("/{code}/athletes/{username}/enroll")
    public Response enrollAthlete(@PathParam("code") Integer code, @PathParam("username") String username, TimeTableDTO[] timeTableDTOs) throws MyEntityAlreadyExistsException {
        try {
            List<Integer> ids = Arrays.stream(timeTableDTOs).map(TimeTableDTO::getId).collect(Collectors.toList());
            List<TimeTable> times = timeTableBean.find(ids);
            if (times.size() != timeTableDTOs.length) {
                return Response.status(Response.Status.NOT_FOUND).entity("TimeTables not found").build();
            }
            Athlete athlete = sportBean.enrollAthlete(username, code, times);
            //TODO: change return
            return Response.status(Response.Status.OK).entity(TimeTableController.toDTOs(athlete.getMySportRegistrations().stream().filter(s -> s.getSport().getCode() == code).map(SportRegistration::getTimeTables).findFirst().orElseThrow(MyConstraintViolationException::new),null)).build();

        } catch (MyEntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity("Athlete already enrolled in sport").build();
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_ATHLETE ----> "+ e.getCause().getMessage(), e);
        }
    }

    @PUT
    @Path("/{code}/athletes/{username}/unroll")
    public Response unrollAthlete(@PathParam("code") Integer code, @PathParam("username") String username) {
        try {
            sportBean.unrollAthlete(username, code);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_ATHLETE", e);
        }
    }

    @POST
    @Path("/teste")
    public Response teste(String time) {
        System.out.println(sportRegistrationBean.find(6));
        sportRegistrationBean.remove(6);
        return Response.ok().entity(sportRegistrationBean.all().size()).build();
    }
}