/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.membership;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 *
 * @author skroflin
 */
public record MembershipResponseDTO(
        Integer id,
        String userFirstName,
        String userLastName,
        LocalDate startDate,
        LocalDate endDate,
        BigInteger membershipPrice
        ) {

}
