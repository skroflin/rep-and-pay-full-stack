/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
@Entity
@Table(name = "training_session")
@AttributeOverride(name = "id", column = @Column(name = "training_session_id"))
public class TrainingSession extends MainEntity {

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type")
    private TrainingType trainingType;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_level")
    private TrainingLevel trainingLevel;
    @Column(name = "beginning_of_session")
    private LocalDateTime beginningOfSession;
    @Column(name = "end_of_session")
    private LocalDateTime endOfSession;
    @Column(name = "already_booked", columnDefinition = "bit")
    private boolean alreadyBooked;

    public TrainingSession() {
    }

    public TrainingSession(User trainer, TrainingType trainingType, TrainingLevel trainingLevel, LocalDateTime beginningOfSession, LocalDateTime endOfSession, boolean alreadyBooked) {
        this.trainer = trainer;
        this.trainingType = trainingType;
        this.trainingLevel = trainingLevel;
        this.beginningOfSession = beginningOfSession;
        this.endOfSession = endOfSession;
        this.alreadyBooked = alreadyBooked;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }

    public void setTrainingLevel(TrainingLevel trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public LocalDateTime getBeginningOfSession() {
        return beginningOfSession;
    }

    public void setBeginningOfSession(LocalDateTime beginningOfSession) {
        this.beginningOfSession = beginningOfSession;
    }

    public LocalDateTime getEndOfSession() {
        return endOfSession;
    }

    public void setEndOfSession(LocalDateTime endOfSession) {
        this.endOfSession = endOfSession;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }

    public void setAlreadyBooked(boolean alreadyBooked) {
        this.alreadyBooked = alreadyBooked;
    }
}
