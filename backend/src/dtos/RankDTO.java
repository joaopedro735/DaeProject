package dtos;

public class RankDTO {
    private int id;

    private String name;

    private Integer sportCode;

    private String sportName;

    public RankDTO() {
    }

    public RankDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RankDTO(int id, String name, int sportCode, String sportName) {
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

    public Integer getSportCode() {
        return sportCode;
    }

    public void setSportCode(Integer sportCode) {
        this.sportCode = sportCode;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
