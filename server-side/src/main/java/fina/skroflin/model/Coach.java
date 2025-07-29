/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.Role;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;

/**
 *
 * @author skroflin
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "coach_id"))
@AttributeOverride(name = "first_name", column = @Column(name = "coach_first_name"))
@AttributeOverride(name = "last_name", column = @Column(name = "coach_last_name"))
@AttributeOverride(name = "email", column = @Column(name = "coach_email"))
@Table(name = "coach", 
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "coach_first_name", name= "uq_coach_first_name"),
            @UniqueConstraint(columnNames = "coach_last_name", name= "uq_coach_last_name"),
            @UniqueConstraint(columnNames = "username", name = "uq_username"),
            @UniqueConstraint(columnNames = "email", name = "uq_coach_email")
        })
public class Coach extends MainEntity{
    private Date dateEmployed;
    private boolean isEmployed;
    private Date dateOfTermination;
    private String bio;
}
