
package com.exemplo.models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private double amount;
    private int clientId;
    private int currencyId;
    
    public Transaction() {}
    
    public Transaction(int clientId, double amount) {
        this.amount = amount;
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "Transaction{" + "amount=" + amount + ", clientId=" + clientId + ", currencyId=" + currencyId + '}';
    }

    
}
