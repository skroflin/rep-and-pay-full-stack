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
    private Users trainer;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type")
    private TrainingType trainingType;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_level")
    private TrainingLevel trainingLevel;
    private int capacity;

    public TrainingSession() {
    }

    public TrainingSession(
            Users trainer,
            LocalDateTime dateTime,
            TrainingType trainingType,
            TrainingLevel trainingLevel,
            int capacity) {
        this.trainer = trainer;
        this.dateTime = dateTime;
        this.trainingType = trainingType;
        this.trainingLevel = trainingLevel;
        this.capacity = capacity;
    }

    public Users getTrainer() {
        return trainer;
    }

    public void setTrainer(Users trainer) {
        this.trainer = trainer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
