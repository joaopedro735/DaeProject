package dtos;

import java.util.Collection;

public class SportDTO {
    private int code;

    private String name;

    private Collection<PartnerDTO> partners;

    private Collection<TrainerDTO> trainers;

    private Collection<AthleteDTO> athletes;

    private Collection<TimeTableDTO> timeTables;

    private Collection<RankDTO> ranks;

    private Collection<GraduationDTO> graduations;

    private float registrationPrice;

    private float membershipPrice;

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

    public Collection<RankDTO> getRanks() {
        return ranks;
    }

    public void setRanks(Collection<RankDTO> ranks) {
        this.ranks = ranks;
    }

    public Collection<GraduationDTO> getGraduations() {
        return graduations;
    }

    public void setGraduations(Collection<GraduationDTO> graduations) {
        this.graduations = graduations;
    }

    public float getRegistrationPrice() {
        return registrationPrice;
    }

    public void setRegistrationPrice(float registrationPrice) {
        this.registrationPrice = registrationPrice;
    }

    public float getMembershipPrice() {
        return membershipPrice;
    }

    public void setMembershipPrice(float membershipPrice) {
        this.membershipPrice = membershipPrice;
    }
}
