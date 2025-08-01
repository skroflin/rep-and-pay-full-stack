/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record MyBookingDTO(
        @Schema(example = "1")
        Integer trainingSessionId,
        @Schema(example = "2025-07-29T20:00:00")
        LocalDateTime reservationTime,
        @Schema(example = "2025-07-29T21:30:00")
        LocalDateTime endOfReservationTime
        ) {

}
