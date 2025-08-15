/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.BookingStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author skroflin
 */
@Data
@Entity
@Table(name = "booking")
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
public class Booking extends MainEntity {

    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "training_session_id")
    private TrainingSession trainingSession;
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    public Booking() {
    }

    public Booking(User user, TrainingSession trainingSession, BookingStatus bookingStatus) {
        this.user = user;
        this.trainingSession = trainingSession;
        this.bookingStatus = bookingStatus;
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
