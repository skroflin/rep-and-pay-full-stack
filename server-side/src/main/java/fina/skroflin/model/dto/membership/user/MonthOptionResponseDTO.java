/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.membership.user;

import java.math.BigDecimal;

/**
 *
 * @author skroflin
 */
public record MonthOptionResponseDTO(
        String monthLabel,
        BigDecimal price,
        boolean paid,
        boolean selectable
        ) {

}
