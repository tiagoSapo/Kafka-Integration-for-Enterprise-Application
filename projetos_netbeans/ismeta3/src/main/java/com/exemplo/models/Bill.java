
package com.exemplo.models;

import java.io.Serializable;

public class Bill implements Serializable {
    
    private int clientId;
    private double billAmount;

    public Bill(int clientId, double billAmount) {
        this.clientId = clientId;
        this.billAmount = billAmount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public String toString() {
        return "Bill{" + "clientId=" + clientId + ", billAmount=" + billAmount + '}';
    }
    
    
    
}
