/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
@Entity
@Table(name = "booking")
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
public class Booking extends MainEntity {

    @ManyToOne
    private User user;
    @ManyToOne
    private TrainingSession trainingSession;
    private LocalDateTime reservationTime;

    public Booking() {
    }

    public Booking(User user, TrainingSession trainingSession, LocalDateTime reservationTime) {
        this.user = user;
        this.trainingSession = trainingSession;
        this.reservationTime = reservationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrainingSession getTrainingSession() {
        return trainingSession;
    }

    public void setTrainingSession(TrainingSession trainingSession) {
        this.trainingSession = trainingSession;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

}
