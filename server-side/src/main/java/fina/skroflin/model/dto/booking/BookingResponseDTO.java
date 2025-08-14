/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.booking;

import fina.skroflin.model.enums.BookingStatus;

/**
 *
 * @author skroflin
 */
public record BookingResponseDTO(
        Integer id,
        String userFirstName,
        String userLastName,
        Integer trainingSessionId,
        BookingStatus bookingStatus
        ) {

}
