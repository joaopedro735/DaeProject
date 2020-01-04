package dtos;

import entities.Purchase;

import java.util.Collection;

public class AthleteDTO extends UserDTO {
    private Collection<SportRegistrationDTO> sportRegistrations;

    private Collection<SportDTO> sportMemberships;

    private Collection<Purchase> purchases;

    public AthleteDTO(String username, String name, String email, String birthday) {
        super(username, name, email, birthday);
    }

    public AthleteDTO() {
        super();
    }

    public Collection<SportRegistrationDTO> getSportRegistrations() {
        return sportRegistrations;
    }

    public void setSportRegistrations(Collection<SportRegistrationDTO> sportRegistrations) {
        this.sportRegistrations = sportRegistrations;
    }

    public Collection<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Collection<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Collection<SportDTO> getSportMemberships() {
        return sportMemberships;
    }

    public void setSportMemberships(Collection<SportDTO> sportMemberships) {
        this.sportMemberships = sportMemberships;
    }
}
