package ws;

import dtos.GraduationDTO;
import ejbs.GraduationBean;
import entities.Graduation;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/graduations")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class GraduationController {

    @EJB
    private GraduationBean graduationBean;

    public static GraduationDTO toDTO(Graduation graduation) {
        GraduationDTO dto = new GraduationDTO(
                graduation.getId(),
                graduation.getName(),
                graduation.getSport().getCode(),
                graduation.getSport().getName()
        );
        return dto;
    }

    public static List<GraduationDTO> toDTOs(Collection<Graduation> graduations) {
        return graduations.stream().map(GraduationController::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createNewGraduation(GraduationDTO graduationDTO) {
        String msg;
        try {
            Graduation graduation = graduationBean.create(graduationDTO.getName(), graduationDTO.getSportCode());

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(graduation))
                    .build();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
    }
}
