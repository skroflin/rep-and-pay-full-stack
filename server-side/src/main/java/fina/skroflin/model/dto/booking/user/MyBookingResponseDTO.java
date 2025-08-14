/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking.user;

import fina.skroflin.model.enums.BookingStatus;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record MyBookingResponseDTO(
        Integer id,
        Integer trainingSessionId,
        BookingStatus bookingStatus
        ) {

}
