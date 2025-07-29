/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;

/**
 *
 * @author skroflin
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "admin_id"))
public class Admin extends MainEntity{
    private List<Client> clients;
    private List<Coach> coaches;

    public Admin() {
    }

    public Admin(List<Client> clients, List<Coach> coaches) {
        this.clients = clients;
        this.coaches = coaches;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<Coach> coaches) {
        this.coaches = coaches;
    }
    
    
}
