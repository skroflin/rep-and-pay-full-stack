/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.user;

import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import fina.skroflin.model.enums.Role;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author skroflin
 */
public record UserResponseDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String username,
        String password,
        Role role,
        List<TrainingSessionResponseDTO> trainingSessions,
        List<BookingResponseDTO> bookings
        ) {
    
}
