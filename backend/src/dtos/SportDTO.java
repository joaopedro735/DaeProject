package dtos;

import entities.Partner;
import entities.Sport;
import entities.TimeTable;

import java.util.Collection;
import java.util.LinkedHashSet;

public class SportDTO {
    private int code;

    private String name;

    private Collection<PartnerDTO> partners;

    private Collection<TrainerDTO> trainers;

    private Collection<UserDTO> athletes;

    private Collection<TimeTableDTO> timeTables;

    public SportDTO() {
        this.partners = new LinkedHashSet<>();
        this.trainers = new LinkedHashSet<>();
        this.athletes = new LinkedHashSet<>();
        this.timeTables = new LinkedHashSet<>();
    }

    public SportDTO(int code, String name) {
        this.code = code;
        this.name = name;
        this.partners = new LinkedHashSet<>();
        this.trainers = new LinkedHashSet<>();
        this.athletes = new LinkedHashSet<>();
        this.timeTables = new LinkedHashSet<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<PartnerDTO> getPartners() {
        return partners;
    }

    public void setPartners(Collection<PartnerDTO> partners) {
        this.partners = partners;
    }

    public Collection<TrainerDTO> getTrainers() {
        return trainers;
    }

    public void setTrainers(Collection<TrainerDTO> trainers) {
        this.trainers = trainers;
    }

    public Collection<UserDTO> getAthletes() {
        return athletes;
    }

    public void setAthletes(Collection<UserDTO> athletes) {
        this.athletes = athletes;
    }

    public Collection<TimeTableDTO> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Collection<TimeTableDTO> timeTables) {
        this.timeTables = timeTables;
    }
}
