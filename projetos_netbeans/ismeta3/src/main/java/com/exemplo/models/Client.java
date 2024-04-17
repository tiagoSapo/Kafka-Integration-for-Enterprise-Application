
package com.exemplo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class Client implements Serializable {
    
    private int id;
    private String table_name;
    
    private String name;
    
    /* Preenchidos pelo KAFKA */
    private double balance; 
    private double credits;
    private double payments;
    private double credits_month;
    
    /* Manager chave estrangeira */
    private int manager_id;
    
    public Client() {
        this.table_name = "0";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public double getPayments() {
        return payments;
    }

    public void setPayments(double payments) {
        this.payments = payments;
    }

    public double getCredits_month() {
        return credits_month;
    }

    public void setCredits_month(double credits_month) {
        this.credits_month = credits_month;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", table_name=" + table_name + ", name=" + name + ", balance=" + balance + ", credits=" + credits + ", payments=" + payments + ", credits_month=" + credits_month + ", manager_id=" + manager_id + '}';
    }
    
    
}
