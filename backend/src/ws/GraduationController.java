package ws;

import dtos.GraduationDTO;
import entities.Graduation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GraduationController {
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
}
