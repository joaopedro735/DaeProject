package dtos;

public class GraduationDTO {
    private int id;

    private String name;

    private int sportCode;

    private String sportName;

    public GraduationDTO() {
    }

    public GraduationDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GraduationDTO(int id, String name, int sportCode, String sportName) {
        this.id = id;
        this.name = name;
        this.sportCode = sportCode;
        this.sportName = sportName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
