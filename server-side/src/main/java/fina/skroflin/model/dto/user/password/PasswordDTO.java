/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.user.password;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author skroflin
 */
public record PasswordDTO(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "pass123")
        String password
        ) {

}
