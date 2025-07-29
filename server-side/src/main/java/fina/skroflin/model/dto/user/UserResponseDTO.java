/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.user;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
        @Enumerated(EnumType.STRING)
        Role role,
        boolean isMembershipPaid,
        @OneToMany(mappedBy = "trainer")
        List<TrainingSession> trainingSessions,
        @OneToMany(mappedBy = "user")
        List<Booking> bookings
        ) {
    
}
