/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author skroflin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeCheckoutResponseDTO {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
