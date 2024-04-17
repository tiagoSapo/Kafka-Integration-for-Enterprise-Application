
package com.exemplo.dto;

import com.exemplo.models.*;
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
    
    public ClientDto(Client cli) {
        if (cli == null) {
            tableName = "0"; 
            balance = 0;
            credits = 0;
            payments = 0;
            creditsMonth = 0;
            return;
        }
        this.balance = cli.getBalance();
        this.manager_id = cli.getManager() != null ? cli.getManager().getId() : null;
        this.name = cli.getName();
        this.credits = cli.getCredits();
        this.payments = cli.getPayments();
        this.creditsMonth = cli.getCreditsMonth();
        this.tableName = "0";
        this.id = cli.getId();
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
    
    public Client toEntity() {
        
        Client c = new Client();
        c.setBalance(balance);
        c.setManager(null);
        c.setName(name);
        c.setTableName(tableName);
        c.setCredits(credits);
        c.setPayments(payments);
        c.setCreditsMonth(creditsMonth);
        return c;
    }
    
    public Client toEntity(Manager m) {
        
        Client c = new Client();
        c.setBalance(balance);
        c.setManager(m);
        m.getClients().add(c);
        c.setName(name);
        c.setTableName(tableName);
        c.setCredits(credits);
        c.setPayments(payments);
        c.setCreditsMonth(creditsMonth);
        return c;
    }
    
}
