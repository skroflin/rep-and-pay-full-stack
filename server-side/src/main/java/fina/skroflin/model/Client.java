/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.Role;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;

/**
 *
 * @author skroflin
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "client_id"))
@AttributeOverride(name = "first_name", column = @Column(name = "client_first_name"))
@AttributeOverride(name = "last_name", column = @Column(name = "client_last_name"))
@AttributeOverride(name = "email", column = @Column(name = "client_email"))
@Table(name = "client", 
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "client_first_name", name= "uq_client_first_name"),
            @UniqueConstraint(columnNames = "client_last_name", name= "uq_client_last_name"),
            @UniqueConstraint(columnNames = "username", name = "uq_username"),
            @UniqueConstraint(columnNames = "email", name = "uq_client_email")
        })
public class Client extends MainEntity{
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    private String email;
    private String password;
    @Column(name = "date_joined")
    private Date dateJoined;

    public Client() {
    }

    public Client(String username, String firstName, String lastName, String email, String password, Date dateJoined) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
    
    

    @PrePersist
    protected void onUserCreation(){
        dateJoined = new Date();
    }
}
