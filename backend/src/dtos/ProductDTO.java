package dtos;

import entities.Type;

import java.util.Collection;

public class ProductDTO {
    private int id;
    protected int typeCode;
    protected String typeName;
    protected String description;
    protected float value;
    private TypeDTO typeDTO;

    public ProductDTO(int id, String description, int type, float value, String typeName) {
        this.typeCode = type;
        this.description = description;
        this.value = value;
        this.typeName = typeName;
        this.id = id;
    }

    public ProductDTO() {

    }

    public TypeDTO getTypeDTO() {
        return typeDTO;
    }

    public void setTypeDTO(TypeDTO typeDTO) {
        this.typeDTO = typeDTO;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
