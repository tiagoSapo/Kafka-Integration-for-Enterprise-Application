
package com.exemplo.dto;

public class CreditsDto {
    
    private int clientId;
    private double amount;

    public CreditsDto(int clientId, double amount) {
        this.clientId = clientId;
        this.amount = amount;
    }
    
    public CreditsDto() {}

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
        return "CreditsPerClientDto{" + "clientId=" + clientId + ", amount=" + amount + '}';
    }
}
