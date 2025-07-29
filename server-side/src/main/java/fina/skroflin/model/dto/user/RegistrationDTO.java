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
        @Schema(name = "Sven")
        String firstName,
        @Schema(name = "Kroflin")
        String lastName,
        @Schema(name = "skroflin")
        String username,
        @Schema(name = "skroflin@fina.hr")
        String email,
        @Schema(name = "lozinka123")
        String password,
        @Schema(name = "superuser")
        Role role
        ) {
    
}
