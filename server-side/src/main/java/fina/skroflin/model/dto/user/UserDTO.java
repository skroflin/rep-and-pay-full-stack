/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author skroflin
 */
public record UserDTO(
        @Schema(example = "skroflin")
        String username,
        @Schema(example = "password123")
        String password
        ) {
}
