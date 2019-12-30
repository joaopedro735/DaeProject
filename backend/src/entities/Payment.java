package entities;

import javax.ejb.Local;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "PAYMENT")
public class Payment /*tem uma Fatura.class */ {
    @Id
    @GeneratedValue
    private int id;

    private LocalDate datePayment;

    private LocalDate limitDayPayment;

    private int quantity;

    private State state;

    private String paymentMethod;

    public Payment(LocalDate datePayment, LocalDate limitDayPayment, int quantity, State state, String paymentMethod) {
        this.datePayment = datePayment;
        this.limitDayPayment = limitDayPayment;
        this.quantity = quantity;
        this.state = state;
        this.paymentMethod = paymentMethod;
    }

    public Payment() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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