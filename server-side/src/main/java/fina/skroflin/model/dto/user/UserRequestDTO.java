/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.user;

import fina.skroflin.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 *
 * @author skroflin
 */
public record UserRequestDTO(
        @Schema(example = "Sven")
        String firstName,
        @Schema(example = "Kroflin")
        String lastName,
        @Schema(example = "skroflin@fina.hr")
        String email,
        @Schema(example = "skroflin")
        String username,
        @Schema(example = "password123")
        String password,
        @Schema(example = "user")
        Role role,
        @Schema(example = "false")
        boolean isMembershipPaid,
        @Schema(example = "2025-07-30")
        LocalDate membershipMonth
        ) {
}
