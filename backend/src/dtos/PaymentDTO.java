package dtos;

import entities.State;

public class PaymentDTO {
    private int id;

    private String datePayment;

    private String limitDayPayment;

    private String state;

    private String paymentMethod;

    private Double value;

    public PaymentDTO() {
    }

    public PaymentDTO(int id, String datePayment, String limitDayPayment, String state, String paymentMethod, Double value) {
        this.id = id;
        this.datePayment = datePayment;
        this.limitDayPayment = limitDayPayment;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.value = value;
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

    public String getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(String datePayment) {
        this.datePayment = datePayment;
    }

    public String getLimitDayPayment() {
        return limitDayPayment;
    }

    public void setLimitDayPayment(String limitDayPayment) {
        this.limitDayPayment = limitDayPayment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
