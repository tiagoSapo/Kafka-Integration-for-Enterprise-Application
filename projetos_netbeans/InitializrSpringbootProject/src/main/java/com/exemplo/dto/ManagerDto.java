
package com.exemplo.dto;

import com.exemplo.models.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagerDto implements Serializable {
    
    private int id;
    private String tableName;
    
    private String name;

    private List<Integer> clients;

    public ManagerDto() {
        tableName = "1";
        clients = new ArrayList<>();
    }

    public ManagerDto(Manager m) {
        
        id = m.getId();
        tableName = m.getTableName();
        name = m.getName();
        
        clients = new ArrayList<>();
        m.getClients().forEach(c -> {clients.add(c.getId());});
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Integer> getClients() {
        return clients;
    }

    public void setClients(List<Integer> clients) {
        this.clients = clients;
    }
    
    public Manager toEntity() {
        Manager m = new Manager();
        m.setTableName(tableName);
        m.setName(name);
        return m;
    }

    @Override
    public String toString() {
        return "ManagerDto{" + "id=" + id + ", tableName=" + tableName + ", name=" + name + ", clients=" + clients + '}';
    }

    
    
}
