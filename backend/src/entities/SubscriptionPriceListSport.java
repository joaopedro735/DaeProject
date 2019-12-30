package entities;

import javax.persistence.*;

@Table(name="SportSubscriptionPriceList")
@Entity
public class SubscriptionPriceListSport {

    @Id
    @OneToOne
    private Sport sport;

    private float price;

    public SubscriptionPriceListSport() {
    }

    public SubscriptionPriceListSport(Sport sport, float price){
        this.sport = sport;
        this.price = price;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
