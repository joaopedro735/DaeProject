package ws;

import dtos.RankDTO;
import dtos.SportDTO;
import entities.Rank;
import entities.Sport;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RankController {
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
}
