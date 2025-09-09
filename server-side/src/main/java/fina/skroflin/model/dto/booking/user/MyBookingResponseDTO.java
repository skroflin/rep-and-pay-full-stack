/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking.user;

import fina.skroflin.model.enums.BookingStatus;
import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record MyBookingResponseDTO(
        LocalDateTime startOfSession,
        LocalDateTime endOfSession,
        TrainingType trainingType,
        TrainingLevel trainingLevel,
        String trainerFirstName,
        String trainerLastName,
        BookingStatus bookingStatus
        ) {

}
