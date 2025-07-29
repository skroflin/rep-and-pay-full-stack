/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking;

import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record BookingResponseDTO(
        Integer id,
        User user,
        TrainingSession trainingSession,
        LocalDateTime reservationTime,
        LocalDateTime endOfReservationTime
        ) {

}
