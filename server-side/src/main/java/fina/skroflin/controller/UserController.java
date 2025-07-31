/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.user.UserResponseDTO;
import fina.skroflin.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<UserResponseDTO> getById(
            @RequestParam int id
    ){
        try {
            if (id <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Id musn't be lesser than 0!"
                );
            }
            UserResponseDTO user = userService.getById(id);
            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with the id" + " " + id
                        + " " + "doesn't exist!"
                );
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error upon fetching user with id"
                + " " + id + " " + e.getMessage(),
                e
            );
        }
    }
}
