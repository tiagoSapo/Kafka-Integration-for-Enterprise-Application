
package com.exemplo.dto;

import java.io.Serializable;

public class CurrencyDto implements Serializable {
    
    private int id;
    private String tableName;
    
    private String name;
    private double exchangeRate;
    
    public CurrencyDto() {tableName = "2";}

    public CurrencyDto(String name, double exchangeRate) {
        this.tableName = "2";
        this.name = name;
        this.exchangeRate = exchangeRate;
    } 

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
        return "CurrencyDto{" + "id=" + id + ", tableName=" + tableName + ", name=" + name + ", exchangeRate=" + exchangeRate + '}';
    }

    
    
}
