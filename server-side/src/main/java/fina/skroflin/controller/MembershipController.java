/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.membership.MembershipRequestDTO;
import fina.skroflin.model.dto.membership.MembershipResponseDTO;
import fina.skroflin.service.MembershipService;
import fina.skroflin.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/post")
    public ResponseEntity<Void> post(
            @RequestBody(required = true) MembershipRequestDTO dto
    ) {
        if (dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The necessary data wasn't inserted!"
            );
        }
        if (dto.userId() != null) {
            try {
                userService.getById(dto.userId());
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Error user with it"
                        + " " + dto.userId()
                        + " " + "doesn't exist!"
                );
            }
        }
        if (dto.startDate() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date is necessary!"
            );
        }
        if (dto.endDate() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date is necessary!"
            );
        }
        if (dto.membershipPrice() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Membership price is necessary!"
            );
        }
        membershipService.post(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
}
