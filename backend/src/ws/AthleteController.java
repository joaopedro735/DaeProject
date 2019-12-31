package ws;

import dtos.AthleteDTO;
import ejbs.AthleteBean;
import entities.Athlete;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/athletes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"Administrator", "Athlete"})
public class AthleteController {
    @EJB
    private AthleteBean athleteBean;

    @Context
    private SecurityContext securityContext;

    public static AthleteDTO toDTO(Athlete athlete, Function<AthleteDTO, AthleteDTO> fn) {
        AthleteDTO dto =  new AthleteDTO(
                athlete.getUsername(),
                athlete.getName(),
                athlete.getEmail(),
                athlete.getBirthday().toString()
        );

        if (fn != null) {
            return fn.apply(dto);
        }
        return dto;
    }

    public static List<AthleteDTO> toDTOs(Collection<Athlete> athletes, Function<AthleteDTO, AthleteDTO> fn) {
        return athletes.stream().map(a -> AthleteController.toDTO(a, null)).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        String msg;
        try {
            GenericEntity<List<AthleteDTO>> entity = new GenericEntity<List<AthleteDTO>>(toDTOs(athleteBean.all(), null)) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_ATHLETES --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }


    @GET
    @Path("/{username}")
    public Response getAthleteDetails(@PathParam("username") String username) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            if (securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
                Athlete athlete = athleteBean.find(username);
                return Response.status(Response.Status.OK).entity(toDTO(athlete, dto -> {
                    dto.setSportRegistrations(SportController.toSportRegistrationDTOs(athlete.getMySportRegistrations(), null));
                    return dto;
                })).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getAthletesBySearch(@PathParam("tosearch") String toSearch){
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            GenericEntity<List<AthleteDTO>> entity = new GenericEntity<List<AthleteDTO>>(toDTOs(athleteBean.findBySearch(toSearch), null)) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/")
    public Response createNewAthlete(AthleteDTO athleteDTO) throws MyEntityAlreadyExistsException, MyConstraintViolationException {

        Athlete athlete = athleteBean.create(athleteDTO.getUsername(),
                athleteDTO.getPassword(),
                athleteDTO.getName(),
                athleteDTO.getEmail(),
                athleteDTO.getBirthday());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(athlete, null))
                .build();

    }

    @PUT
    @Path("/{username}")
    public Response updateAthlete(@PathParam("username") String username, AthleteDTO athleteDTO){
        String msg;
        User user;
        System.out.println(username);
        try {
            user = athleteBean.find(username);
            if (user == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            athleteBean.update(username, athleteDTO.getPassword(), athleteDTO.getName(), athleteDTO.getEmail());

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
    public Response deleteAthlete(@PathParam("username") String username) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            athleteBean.remove(username);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            msg = "ERROR_DELETING_PARTNER --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
