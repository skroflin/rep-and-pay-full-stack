/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.Role;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;
import java.util.List;

/**
 *
 * @author skroflin
 */
@Entity
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
    @Lob
    private String bio;
    @OneToMany(mappedBy = "coach")
    private List<TrainingSession> trainingSessions; 

    public Coach() {
    }

    public Coach(Date dateEmployed, boolean isEmployed, Date dateOfTermination, String bio, List<TrainingSession> trainingSessions) {
        this.dateEmployed = dateEmployed;
        this.isEmployed = isEmployed;
        this.dateOfTermination = dateOfTermination;
        this.bio = bio;
        this.trainingSessions = trainingSessions;
    }

    public Date getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(Date dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public boolean isIsEmployed() {
        return isEmployed;
    }

    public void setIsEmployed(boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    public Date getDateOfTermination() {
        return dateOfTermination;
    }

    public void setDateOfTermination(Date dateOfTermination) {
        this.dateOfTermination = dateOfTermination;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }
    
    @PrePersist
    protected void onEmployment(){
        dateEmployed = new Date();
    }
}
