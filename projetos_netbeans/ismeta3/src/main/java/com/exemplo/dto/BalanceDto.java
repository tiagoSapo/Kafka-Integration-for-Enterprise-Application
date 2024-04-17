
package com.exemplo.dto;

public class BalanceDto {
    
    private int clientId;
    private double balance;

    public BalanceDto(int clientId, double balance) {
        this.clientId = clientId;
        this.balance = balance;
    }
    
    public BalanceDto() {}

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    @Override
    public String toString() {
        return "BalanceDto{" + "clientId=" + clientId + ", balance=" + balance + '}';
    }
}
