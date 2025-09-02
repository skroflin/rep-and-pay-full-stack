/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import fina.skroflin.service.MembershipService;
import fina.skroflin.utils.stripe.StripeConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author skroflin
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/fina/skroflin/stripe")
public class MembershipWebhookController {

    private final MembershipService membershipService;
    private final StripeConfig stripeConfig;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload
    ) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(
                payload,
                sigHeader,
                stripeConfig.stripeWebhookSecret()
        );

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (session != null) {
                Integer userId = Integer.parseInt(session.getClientReferenceId());
                int price = session.getAmountTotal().intValue();
                membershipService.activateMembership(userId, 30, price);
            }
        }
        return new ResponseEntity<>("Webhook received!", HttpStatus.OK);
    }

}
