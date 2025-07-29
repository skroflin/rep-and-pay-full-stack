/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.booking.BookingDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import jakarta.transaction.Transactional;

/**
 *
 * @author skroflin
 */
public class BookingService extends MainService {
    
    @Transactional
    private BookingResponseDTO convertToResponseDTO(Booking booking){
        if (booking == null) {
            return null;
        }
        Integer userId = (booking.getId() != null) ? booking.getUser().getId() : null;
        Integer trainingSessionId = (booking.getId() != null) ? booking.getTrainingSession().getId() : null;
        return new BookingResponseDTO(
                booking.getId(), 
                userId,
                trainingSessionId,
                booking.getReservationTime(),
                booking.getEndOfReservation()
        );
    }
    
    @Transactional
    private Booking convertToEntity(BookingDTO dto){
        Booking booking = new Booking();
        if (dto.userId() != null) {
            User user = session.get(User.class, dto.userId());
            if (user == null) {
                throw new IllegalArgumentException("User with the id" + " " + dto.userId() + " " + "doesn't exist!");
            }
            booking.setUser(user);
        }
        if (dto.trainingSessionId() != null) {
            TrainingSession trainingSession = session.get(
                    TrainingSession.class, dto.trainingSessionId());
            if (trainingSession == null) {
                throw new IllegalArgumentException("Training session with the id" + " " + dto.userId() + " " + "doesn't exist!");
            }
            booking.setTrainingSession(trainingSession);
        }
        booking.setReservationTime(dto.reservationTime());
        booking.setEndOfReservation(dto.endOfReservationTime());
        return booking;
    }
    
    private void updateEntityFromDto(Booking booking, BookingDTO dto) {
        
    }
}
