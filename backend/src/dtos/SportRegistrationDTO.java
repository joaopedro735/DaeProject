package dtos;

import java.io.Serializable;
import java.util.Collection;

public class SportRegistrationDTO implements Serializable {
    private int id;

    private int sportCode;

    private String sportName;

    private String athleteUsername;

    private String athleteName;

    private RankDTO rank;

    private GraduationDTO graduation;

    private Collection<TimeTableDTO> timeTables;

    public SportRegistrationDTO() {
    }

    public SportRegistrationDTO(int id, int sportCode, String sportName, String athleteUsername, String athleteName, RankDTO rank, GraduationDTO graduation) {
        this.id = id;
        this.sportCode = sportCode;
        this.sportName = sportName;
        this.athleteUsername = athleteUsername;
        this.athleteName = athleteName;
        this.rank = rank;
        this.graduation = graduation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSportCode() {
        return sportCode;
    }

    public void setSportCode(int sportCode) {
        this.sportCode = sportCode;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getAthleteUsername() {
        return athleteUsername;
    }

    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public RankDTO getRank() {
        return rank;
    }

    public void setRank(RankDTO rank) {
        this.rank = rank;
    }

    public GraduationDTO getGraduation() {
        return graduation;
    }

    public void setGraduation(GraduationDTO graduation) {
        this.graduation = graduation;
    }

    public Collection<TimeTableDTO> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Collection<TimeTableDTO> timeTables) {
        this.timeTables = timeTables;
    }
}
