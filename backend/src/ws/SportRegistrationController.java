package ws;

import dtos.GraduationDTO;
import dtos.RankDTO;
import dtos.SportRegistrationDTO;
import dtos.TimeTableDTO;
import entities.SportRegistration;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SportRegistrationController {

    public static SportRegistrationDTO toSportRegistrationDTO(SportRegistration sportRegistration, Function<SportRegistrationDTO, SportRegistrationDTO> fn) {
        SportRegistrationDTO dto = new SportRegistrationDTO(
                sportRegistration.getId(),
                sportRegistration.getSport().getCode(),
                sportRegistration.getSport().getName(),
                sportRegistration.getAthlete().getUsername(),
                sportRegistration.getAthlete().getName()
        );

        if (sportRegistration.getRank() != null) {
            RankDTO rankDTO = RankController.toDTO(sportRegistration.getRank());
            dto.setRank(rankDTO);
        }
        if (sportRegistration.getGraduation() != null) {
            //todo: change to GraduationControler.toDTO
            GraduationDTO graduationDTO = GraduationController.toDTO(sportRegistration.getGraduation());
            dto.setGraduation(graduationDTO);
        }

        if (sportRegistration.getTimeTables() != null) {
            List<TimeTableDTO> timeTableDTOS = TimeTableController.toDTOs(sportRegistration.getTimeTables(), null);
            dto.setTimeTables(timeTableDTOS);
        }

        if (fn != null) {
            return fn.apply(dto);
        }
        return dto;
    }

    public static List<SportRegistrationDTO> toSportRegistrationDTOs(Collection<SportRegistration> sportRegistrations, Function<SportRegistrationDTO, SportRegistrationDTO> fn) {
        return sportRegistrations.stream().map(s -> toSportRegistrationDTO(s, fn)).collect(Collectors.toList());
    }
}
