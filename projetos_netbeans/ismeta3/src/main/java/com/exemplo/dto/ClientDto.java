package com.exemplo.dto;

import java.io.Serializable;

public class ClientDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tableName;

    private int id;
    private String name;

    /* Preenchidos pelo KAFKA */
    private double balance;
    private double credits;
    private double payments;
    private double creditsMonth;

    private int manager_id;

    public ClientDto() {
        tableName = "0";
        balance = 0;
    }

    public ClientDto(String name, int manager_id) {
        this();
        this.name = name;
        this.manager_id = manager_id;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
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

    public double getCreditsMonth() {
        return creditsMonth;
    }

    public void setCreditsMonth(double creditsMonth) {
        this.creditsMonth = creditsMonth;
    }

}
