/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.user.UserDTO;
import fina.skroflin.model.dto.user.UserResponseDTO;
import fina.skroflin.service.user.UserService;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RestController
@RequestMapping("/api/fina/skroflin/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    
    @GetMapping("/get")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        try {
            return new ResponseEntity<>(
                    userService.getAll(), HttpStatus.OK
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon fetching" + " " + e.getMessage(),
                    e
            );
        }
    }
    
    @GetMapping("/getById")
    public ResponseEntity<UserResponseDTO> getByToken(
            @RequestHeader
            HttpHeaders headers
    ){
        try {
            return ResponseEntity.ok(userService.getById(headers));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                    "Invalid or missing token:" 
                            + " " + e.getMessage(), 
                    e
            );
        }
    }
    
    @PutMapping("/put")
    public ResponseEntity<UserResponseDTO> put(
            @RequestParam int id,
            @RequestBody(required = true)
            UserDTO dto
    ){
        try {
            if (id <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Id musn't be lesser than 0!"
                );
            }
            if (dto.firstName() == null 
                    || dto.firstName().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "First name is necessary!"
                );
            }
            if (dto.lastName() == null 
                    || dto.lastName().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Last name is necessary!"
                );
            }
            if (dto.email()== null 
                    || dto.email().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email is necessary!"
                );
            }
            if (dto.username()== null 
                    || dto.username().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Username is necessary!"
                );
            }
            if (dto.password()== null || dto.password().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Password is necessary!"
                );
            }
            if (dto.role()== null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Role is necessary!"
                );
            }
            if (dto.membershipMonth() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Membership month is necessary!"
                );
            }
            
            UserResponseDTO updatedUser = userService.put(dto, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon updating user with id" 
                    + " " + id + " " + e.getMessage(),
                    e
            );
        }
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int id
    ){
        try {
            if (id <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Id musn't be lesser than 0!"
                );
            }
            userService.delete(id);
            return new ResponseEntity<>(
                    "User with id" 
                        + " " + id 
                        + " " + "deleted",
                    HttpStatus.OK
            );
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon deletion" + " " + e.getMessage(),
                    e
            );
        }
    }
}
