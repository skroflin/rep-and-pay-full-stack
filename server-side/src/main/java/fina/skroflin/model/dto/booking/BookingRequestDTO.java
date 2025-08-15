/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking;

import fina.skroflin.model.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 *
 * @author skroflin
 */
@Builder
public record BookingRequestDTO(
        @Schema(example = "1")
        Integer userId,
        @Schema(example = "1")
        Integer trainingSessionId,
        @Schema(example = "pending")
        BookingStatus bookingStatus
        ) {

}
