
package com.exemplo.models;

import java.io.Serializable;

public class Manager implements Serializable {
    
    private int id;
    
    private String name;
    private String table_name;

    public Manager() {
        table_name = "1";
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

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", name=" + name + ", table_name=" + table_name + '}';
    }

    
}
