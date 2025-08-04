/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.user;

import fina.skroflin.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author skroflin
 */
public record RegistrationDTO(
        @Schema(example = "Sven")
        String firstName,
        @Schema(example = "Kroflin")
        String lastName,
        @Schema(example = "skroflin")
        String username,
        @Schema(example = "skroflin@fina.hr")
        String email,
        @Schema(example = "lozinka123")
        String password,
        @Schema(example = "superuser")
        Role role
        ) {
    
}
