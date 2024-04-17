
package com.exemplo.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;


@Entity
public class Client implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String tableName;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name; 
    
    /* Preenchidos pelo KAFKA */
    private double balance; 
    private double credits;
    private double payments;
    private double creditsMonth;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;
 
    public Client() {
        tableName = "0"; 
        balance = 0;
        credits = 0;
        payments = 0;
        creditsMonth = 0;
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

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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

    @Override
    public String toString() {
        return "Client{" + "tableName=" + tableName + ", id=" + id + ", name=" + name + ", balance=" + balance + ", credits=" + credits + ", payments=" + payments + ", creditsMonth=" + creditsMonth + ", manager=" + manager + '}';
    }

    
    
    
}
