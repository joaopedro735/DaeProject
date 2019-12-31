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

    private Collection<AthleteDTO> athletes;

    private Collection<TimeTableDTO> timeTables;

    public SportDTO() {
    }

    public SportDTO(int code, String name) {
        this.code = code;
        this.name = name;
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

    public Collection<AthleteDTO> getAthletes() {
        return athletes;
    }

    public void setAthletes(Collection<AthleteDTO> athletes) {
        this.athletes = athletes;
    }

    public Collection<TimeTableDTO> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Collection<TimeTableDTO> timeTables) {
        this.timeTables = timeTables;
    }
}
