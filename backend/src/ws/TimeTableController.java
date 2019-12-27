package ws;

import dtos.SportDTO;
import dtos.TimeTableDTO;
import ejbs.TimeTableBean;
import entities.Sport;
import entities.TimeTable;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/timetables")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TimeTableController {

    @EJB
    private TimeTableBean timeTableBean;

    @Context
    private SecurityContext securityContext;

    public static TimeTableDTO toDTO(TimeTable timeTable, Function<TimeTableDTO, TimeTableDTO> fn) {
        TimeTableDTO dto = new TimeTableDTO(
                timeTable.getId(),
                timeTable.getDay(),
                timeTable.getStart(),
                timeTable.getEnd(),
                timeTable.getSport().getCode(),
                timeTable.getSport().getName()
        );

        if (fn != null) {
            return fn.apply(dto);
        }
            return dto;
    }

    public static List<TimeTableDTO> toDTOs(Collection<TimeTable> timeTables, Function<TimeTableDTO, TimeTableDTO> fn) {
        return timeTables.stream().map(s -> TimeTableController.toDTO(s, fn)).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public Response all() {
        String msg;
        try {
            GenericEntity<List<TimeTableDTO>> entity = new GenericEntity<List<TimeTableDTO>>(toDTOs(timeTableBean.all(), null)) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_TIMETABLES --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/teste")
    public Response teste(String time) {
        System.out.println(time);
        return Response.ok().entity(time).build();
    }
}
