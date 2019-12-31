package dtos;

import com.sun.xml.internal.ws.api.server.AbstractInstanceResolver;
import entities.Sport;

import java.io.Serializable;
import java.util.Collection;

public class TrainerDTO extends UserDTO {
    private Collection<Sport> sports;

    public TrainerDTO(String username, String name, String email, String birthday) {
        super(username, name, email, birthday);
    }

    public TrainerDTO() {
        super();
    }
}
