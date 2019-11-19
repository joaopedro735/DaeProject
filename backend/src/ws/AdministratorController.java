package ws;

import dtos.AdministratorDTO;
import ejbs.AdministratorBean;
import entities.Administrator;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorController {
    @EJB
    private AdministratorBean administratorBean;

    public static AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getName(),
                administrator.getEmail()
        );
    }

    public static List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(AdministratorController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<AdministratorDTO> all() {
        try {
            return toDTOs(administratorBean.all());
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_STUDENTS", e);
        }
    }
}
