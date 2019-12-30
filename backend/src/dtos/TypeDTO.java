package dtos;

public class TypeDTO {
    private int id;
    private String name;

    public TypeDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TypeDTO() {
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
}
