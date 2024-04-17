
package com.exemplo.dto;

import com.exemplo.models.*;
import java.io.Serializable;

public class CurrencyDto implements Serializable {
    
    private int id;
    private String tableName;
    
    private String name;
    private double exchangeRate;
    
    public CurrencyDto() {tableName = "2";}

    public CurrencyDto(String name, double exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    } 

    public CurrencyDto(Currency c) {
        if (c == null) {
            tableName = "2";
            return;
        }
        this.tableName = c.getTableName();
        this.name = c.getName().toString();
        this.exchangeRate = c.getExchangeRate();
        this.id = c.getId();
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
    
    public Currency toEntity() {
        Currency c = new Currency();
        c.setExchangeRate(exchangeRate);
        c.setTableName(tableName);
        if (name.equals(CurrencyName.DOLLAR.toString())) {
            c.setName(CurrencyName.DOLLAR);
        }
        else if (name.equals(CurrencyName.YEN.toString())) {
            c.setName(CurrencyName.YEN);
        }
        else if (name.equals(CurrencyName.POUND.toString())) {
            c.setName(CurrencyName.POUND);
        }
        else {
            c.setName(CurrencyName.EURO);
        }
        return c;
    }

    @Override
    public String toString() {
        return "CurrencyDto{" + "id=" + id + ", tableName=" + tableName + ", name=" + name + ", exchangeRate=" + exchangeRate + '}';
    }

    
    
}
