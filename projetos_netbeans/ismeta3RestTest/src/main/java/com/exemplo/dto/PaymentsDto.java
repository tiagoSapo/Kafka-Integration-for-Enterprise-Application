
package com.exemplo.dto;

public class PaymentsDto {
    
    private int clientId;
    private double amount;

    public PaymentsDto(int clientId, double amount) {
        this.clientId = clientId;
        this.amount = amount;
    }
    
    public PaymentsDto() {}

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "PaymentsDto{" + "clientId=" + clientId + ", amount=" + amount + '}';
    }
}
