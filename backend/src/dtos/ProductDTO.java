package dtos;

import java.math.BigDecimal;

public class ProductDTO {
    private int id;
    protected int typeCode;
    protected String typeName;
    protected String description;
    protected @javax.validation.constraints.NotNull BigDecimal value;
    private TypeDTO typeDTO;

    public ProductDTO(int id, String description, int type, @javax.validation.constraints.NotNull BigDecimal value, String typeName) {
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

    public @javax.validation.constraints.NotNull BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
