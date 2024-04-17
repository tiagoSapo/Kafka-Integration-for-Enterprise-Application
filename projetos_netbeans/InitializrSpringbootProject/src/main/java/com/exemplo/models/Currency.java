
package com.exemplo.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Currency implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tableName;
    
    @Enumerated(EnumType.STRING)
    private CurrencyName name;
    private double exchangeRate;
    
    public Currency() {tableName = "2";}

    public Currency(CurrencyName name, double exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    } 

    public CurrencyName getName() {
        return name;
    }

    public void setName(CurrencyName name) {
        this.name = name;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "Currency{" + "id=" + id + ", tableName=" + tableName + ", name=" + name + ", exchangeRate=" + exchangeRate + '}';
    }

    
    
}
