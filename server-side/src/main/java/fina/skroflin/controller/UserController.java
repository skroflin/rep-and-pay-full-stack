/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.User;
import fina.skroflin.model.dto.user.LoginDTO;
import fina.skroflin.model.dto.user.RegistrationDTO;
import fina.skroflin.model.dto.user.UserDTO;
import fina.skroflin.model.dto.user.UserResponseDTO;
import fina.skroflin.service.UserService;
import fina.skroflin.utils.jwt.JwtResponse;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@Tag(name = "User", description = "Available endpoints for the entity 'Users'")
@RestController
@RequestMapping("/api/fina/skroflin/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
            JwtTokenUtil jwtTokenUtil, 
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }
    
    @Operation(
            summary = "Retrieves all users", tags = {"get", "user"},
            description = "Retrieves all users (superuser, coach or user) with information about"
                    + " " + "their first name, last name, email, username, password, role, have"
                    + " " + "they paid for their membership or not, membership month, list of"
                    + " " + "training sessions (if they are trainers/coaches) and lists of bookings"
                    + " " + "(if they are users)."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
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
    
    @Operation(
            summary = "Retrieves user data of the user in the session", tags = {"get", "user", "getMyProfile"},
            description = "Retrieves data of the current authentiacted user with information about"
                    + " " + "their first name, last name, email, username, password, role, have"
                    + " " + "they paid for their membership or not, membership month, list of"
                    + " " + "training sessions (if they are trainers/coaches) and lists of bookings"
                    + " " + "(if they are users)."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/getMyProfile")
    public ResponseEntity<UserResponseDTO> getByToken(
            @RequestHeader
            HttpHeaders headers
    ){
        try {
            return ResponseEntity.ok(userService.getMyProfile(headers));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                    "Invalid or missing token:" 
                            + " " + e.getMessage(), 
                    e
            );
        }
    }
    
    @Operation(
            summary = "Updates user profile", tags = {"put", "updateMyProfile", "user"},
            description = "Updates user profile that is currently logged"
                    + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/updateMyProfile")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @RequestHeader
            HttpHeaders headers,
            @RequestBody(required = true)
            UserDTO dto
    ){
        try {
            UserResponseDTO updatedUser = userService.updateMyProfile(headers, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon updating user" + " "
                            + dto.username() + " " 
                            + e.getMessage(),
                    e
            );
        }
    }
    
    @Operation(
            summary = "Updates user", tags = {"put", "user"},
            description = "Updates user.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the user in the database, must be greater than 0!",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Bad request (id wasn't received or dto object)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Deletes user profile", tags = {"delete", "deleteMyProfile", "user"},
            description = "Deletes user profile of the user that is" + " "
                    + "currently logged" + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                description = "Deleted", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", 
                description = "Bad request (can't delete because there is no user or some users are in certain training session)", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", 
                description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/deleteMyProfile")
    public ResponseEntity<String> deleteMyProfile(
            @RequestHeader
            HttpHeaders headers
    ){
        try {
            userService.deleteMyProfile(headers);
            return ResponseEntity.ok(
                    "Profile sucesfully deleted!"
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
    
    @Operation(
            summary = "Deletes user", tags = {"delete", "user"},
            description = "Deletes user.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the user in the database, must be greater than 0!",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                description = "Updated", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", 
                description = "Bad request (can't delete because there is no user or some users are in certain training session)", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", 
                description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody(required = true)
            RegistrationDTO dto
    ){
        try {
            UserResponseDTO registeredUser = userService.registration(dto);
            return new ResponseEntity<>(registeredUser, HttpStatus.OK);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon registration" + " " + e.getMessage(),
                    e
            );
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody(required = true)
            LoginDTO dto
    ){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
            User user = userService.getUserByEmail(dto.username());
            String jwt = jwtTokenUtil.generateToken(user, user.getId());
            
            return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.getRole()));
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon login" + " " + e.getMessage(),
                    e
            );
        }
    }
}
