/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.membership.MembershipResponseDTO;
import fina.skroflin.model.dto.membership.user.MyMembershipResponseDTO;
import fina.skroflin.model.dto.stripe.CheckoutSessionRequestDTO;
import fina.skroflin.model.dto.stripe.StripeCheckoutResponseDTO;
import fina.skroflin.service.MembershipService;
import fina.skroflin.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author skroflin
 */
@Tag(name = "Membership", description = "Available endpoints for the entity 'Membership'")
@RestController
@RequestMapping("/api/fina/skroflin/membership")
@Slf4j
public class MembershipController {

    private final UserService userService;
    private final MembershipService membershipService;

    public MembershipController(
            UserService userService,
            MembershipService membershipService
    ) {
        this.userService = userService;
        this.membershipService = membershipService;
    }

    @GetMapping("/getActiveMemberships")
    public ResponseEntity<List<MembershipResponseDTO>> getActiveMemberships() {
        List<MembershipResponseDTO> memberships
                = membershipService.getActiveMemberships();
        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @GetMapping("/getExpiredMemberships")
    public ResponseEntity<List<MembershipResponseDTO>> getExpiredMemberships() {
        List<MembershipResponseDTO> memberships
                = membershipService.getExpiredMemberships();
        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @GetMapping("/getMembershipByUser")
    public ResponseEntity<List<MembershipResponseDTO>> getMembershipByUser(
            @RequestParam int userId
    ) {
        if (userId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User id musn't be lesser than 0!"
            );
        }
        List<MembershipResponseDTO> memberships
                = membershipService.getMembershipByUser(userId);
        if (memberships == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User with id" + " "
                    + userId + " " + "doesn't exist!"
            );
        }

        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @GetMapping("/hasActiveMembership")
    public ResponseEntity<Boolean> hasActiveMembership(
            @RequestHeader HttpHeaders headers
    ) {
        boolean activeMembership = membershipService.hasActiveMembership(headers);
        return new ResponseEntity<>(activeMembership, HttpStatus.OK);
    }

    @PostMapping("/createCheckoutSession")
    public ResponseEntity<String> createCheckoutSession(
            @RequestHeader HttpHeaders headers,
            @RequestBody CheckoutSessionRequestDTO dto
    ) {
        String checkoutUrl = membershipService.createCheckoutSession(headers, dto);
        return new ResponseEntity<>(checkoutUrl, HttpStatus.OK);
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return new ResponseEntity<>("Payment successful!", HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancelled() {
        return new ResponseEntity<>("Payment cancelled!", HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @RequestParam String status
    ) {
        if ("success".equals(status)) {
            return new ResponseEntity<>("Payment confirmed", HttpStatus.OK);
        } else if ("cancel".equals(status)) {
            return new ResponseEntity<>("Payment cancelled", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getMyMemberships")
    public ResponseEntity<List<MyMembershipResponseDTO>> getMyMemberships(
            @RequestHeader HttpHeaders headers
    ) {
        List<MyMembershipResponseDTO> myMemberships = membershipService.getMyMemberships(headers);
        return new ResponseEntity<>(myMemberships, HttpStatus.OK);
    }

    @GetMapping("/getNumOfMemberships")
    public ResponseEntity<Long> getNumOfMemberships() {
        Long numOfMyMemberships = membershipService.getNumOfMemberships();
        return new ResponseEntity<>(numOfMyMemberships, HttpStatus.OK);
    }

    @GetMapping("/getNumOfActiveMemberships")
    public ResponseEntity<Long> getNumOfActiveMemberships() {
        Long numOfMyActiveMemberships = membershipService.getNumOfActiveMemberships();
        return new ResponseEntity<>(numOfMyActiveMemberships, HttpStatus.OK);
    }

    @GetMapping("/getNumOfExpiredMemberships")
    public ResponseEntity<Long> getNumOfExpiredMemberships() {
        Long numOfExpiredMemberships = membershipService.getNumOfExpiredMemberships();
        return new ResponseEntity<>(numOfExpiredMemberships, HttpStatus.OK);
    }

    @GetMapping("/getAllMemberships")
    public ResponseEntity<List<MembershipResponseDTO>> getAllMemberships() {
        List<MembershipResponseDTO> memberships = membershipService.getAllMemberships();
        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }
}
