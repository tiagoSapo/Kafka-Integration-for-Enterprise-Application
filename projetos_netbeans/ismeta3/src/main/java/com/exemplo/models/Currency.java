
package com.exemplo.models;

import java.io.Serializable;

public class Currency implements Serializable {
    
    private int id;
    private String table_name;
    
    private String name;
    private double exchange_rate;
    
    public Currency() {
        table_name = "2";
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

    public double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    @Override
    public String toString() {
        return "Currency{" + "id=" + id + ", table_name=" + table_name + ", name=" + name + ", exchange_rate=" + exchange_rate + '}';
    }
    
    
}
