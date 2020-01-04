package entities;

import javax.ejb.Local;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@NamedQueries({
        @NamedQuery(
                name = "getAllPayments",
                query = "SELECT p FROM Payment p ORDER BY p.id"
        )
})
@Entity
@Table(name = "PAYMENT")
public class Payment /*tem uma Fatura.class */ {
    @Id
    @GeneratedValue
    private int id;

    private LocalDate datePayment;

    private LocalDate limitDayPayment;

    private State state;

    private String paymentMethod;

    private Double value;

    public Payment(LocalDate limitDayPayment, State state, String paymentMethod, Double value) {
        this.datePayment = LocalDate.now();
        this.limitDayPayment = limitDayPayment;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.value = value;
    }

    public Payment() {
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(LocalDate datePayment) {
        this.datePayment = datePayment;
    }

    public LocalDate getLimitDayPayment() {
        return limitDayPayment;
    }

    public void setLimitDayPayment(LocalDate limitDayPayment) {
        this.limitDayPayment = limitDayPayment;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Version
    private int version;
    /*
    list payment
    list products
    compradoA
    preco
    descontos/vouchers
     */
}
