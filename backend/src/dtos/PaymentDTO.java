package dtos;

import entities.State;

import java.time.LocalDate;

public class PaymentDTO {
    private int id;

    private LocalDate datePayment;

    private LocalDate limitDayPayment;

    private State state;

    private String paymentMethod;

    public PaymentDTO(int id, LocalDate datePayment, LocalDate limitDayPayment, State state, String paymentMethod) {
        this.id = id;
        this.datePayment = datePayment;
        this.limitDayPayment = limitDayPayment;
        this.state = state;
        this.paymentMethod = paymentMethod;
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
}
