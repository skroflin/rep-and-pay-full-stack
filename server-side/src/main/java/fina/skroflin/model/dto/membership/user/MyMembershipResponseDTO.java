/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.membership.user;

import java.time.LocalDate;

/**
 *
 * @author skroflin
 */
public record MyMembershipResponseDTO(
        LocalDate startDate,
        LocalDate endDate,
        long membershipPrice,
        LocalDate paymentDate
        ) {
}
