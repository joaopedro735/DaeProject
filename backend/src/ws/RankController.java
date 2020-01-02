package ws;

import dtos.RankDTO;
import dtos.SportDTO;
import dtos.TrainerDTO;
import ejbs.RankBean;
import entities.Partner;
import entities.Rank;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RankController {
    @EJB
    private RankBean rankBean;

    public static RankDTO toDTO(Rank rank) {
        RankDTO dto = new RankDTO(
                rank.getId(),
                rank.getName(),
                rank.getSport().getCode(),
                rank.getSport().getName()
        );
        return dto;
    }

    public static List<RankDTO> toDTOs(Collection<Rank> ranks) {
        return ranks.stream().map(RankController::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createNewRank(RankDTO rankDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            Rank rank = rankBean.create(rankDTO.getName(), rankDTO.getSportCode());

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(rank))
                    .build();
        } catch (MyEntityAlreadyExistsException | MyConstraintViolationException | MyEntityNotFoundException e) {
            msg = e.getMessage();
            //System.out.println(e.getCause().getMessage());
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
    }
}
