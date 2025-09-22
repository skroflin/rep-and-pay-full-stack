/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.membership;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 *
 * @author skroflin
 */
public record MembershipRequestDTO(
        @Schema(example = "3")
        Integer userId,
        @Schema(example = "2025-08-01")
        LocalDate startDate,
        @Schema(example = "2025-09-01")
        LocalDate endDate,
        @Schema(example = "29.99")
        long membershipPrice,
        @Schema(example = "true")
        boolean alreadyPaid
        ) {

}
