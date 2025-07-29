/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.ConfirmationStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "training_session_id"))
@Table(name = "training_session")
public class TrainingSession extends MainEntity{
    private Coach coach;
    private Client client;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String trainingType;
    private boolean isBooked;
    private String trainingNotes;
    private ConfirmationStatus confirmationStatus;

    public TrainingSession() {
    }

    public TrainingSession(Coach coach, Client client, LocalDateTime startTime, LocalDateTime endTime, String trainingType, boolean isBooked, String trainingNotes, ConfirmationStatus confirmationStatus) {
        this.coach = coach;
        this.client = client;
        this.startTime = startTime;
        this.endTime = endTime;
        this.trainingType = trainingType;
        this.isBooked = isBooked;
        this.trainingNotes = trainingNotes;
        this.confirmationStatus = confirmationStatus;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public boolean isIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public String getTrainingNotes() {
        return trainingNotes;
    }

    public void setTrainingNotes(String trainingNotes) {
        this.trainingNotes = trainingNotes;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }
    
    
}
