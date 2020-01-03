package ws;

import dtos.PartnerDTO;
import dtos.TrainerDTO;
import ejbs.PartnerBean;
import ejbs.SportBean;
import ejbs.TrainerBean;
import entities.Partner;
import entities.Sport;
import entities.Trainer;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/partners")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class PartnerController {
    @EJB
    private PartnerBean partnerBean;

    @EJB
    private SportBean sportBean;

    @Context
    private SecurityContext securityContext;

    public static PartnerDTO toDTO(Partner partner) {
        return new PartnerDTO(
                partner.getUsername(),
                partner.getName(),
                partner.getEmail(),
                partner.getBirthday().toString()
        );
    }

    public static List<PartnerDTO> toDTOs(Collection<Partner> partners) {
        return partners.stream().map(ws.PartnerController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        String msg;
        try {
            GenericEntity<List<PartnerDTO>> entity = new GenericEntity<List<PartnerDTO>>(toDTOs(partnerBean.all())) {
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
    @Path("/partner/{username}")
    public Response getPartnerDetails(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        System.out.println(principal.getName());
        if (securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            Partner partner = partnerBean.find(username);
            return Response.status(Response.Status.OK).entity(toDTO(partner)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getPartnersBySearch(@PathParam("tosearch") String toSearch){
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            GenericEntity<List<PartnerDTO>> entity = new GenericEntity<List<PartnerDTO>>(toDTOs(partnerBean.findBySearch(toSearch))) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/")
    public Response createNewPartner(TrainerDTO trainerDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        Partner partner = partnerBean.create(trainerDTO.getUsername(),
                trainerDTO.getPassword(),
                trainerDTO.getName(),
                trainerDTO.getEmail(),
                trainerDTO.getBirthday());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(partner))
                .build();

    }

    @PUT
    @Path("/{username}")
    public Response updatePartner(@PathParam("username") String username, TrainerDTO trainerDTO){
        String msg;
        User user;
        System.out.println(username);
        try {
            user = partnerBean.find(username);
            if (user == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            partnerBean.update(username, trainerDTO.getPassword(), trainerDTO.getName(), trainerDTO.getEmail());

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
    public Response deletePartner(@PathParam("username") String username) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            partnerBean.remove(username);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            msg = "ERROR_DELETING_PARTNER --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("/availableToEnroll/{sportCode}")
    public Response availableToEnrollToSport(@PathParam("sportCode") Integer sportCode, @QueryParam("name") String nameToSearch) {
        String msg;
        System.out.println(nameToSearch);
        try {
            Sport sport = sportBean.find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found");
            }
            List<Partner> allAvailable = partnerBean.findBySearch(nameToSearch).stream().filter(p -> !p.getSports().contains(sport)).collect(Collectors.toList());
            GenericEntity<List<PartnerDTO>> entity = new GenericEntity<List<PartnerDTO>>(toDTOs(allAvailable)) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_TRAINERS_AVAILABLE_TO_SUBSCRIBE --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
