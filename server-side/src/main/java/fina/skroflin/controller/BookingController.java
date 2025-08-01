/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.Booking;
import fina.skroflin.model.Users;
import fina.skroflin.model.dto.booking.BookingDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.booking.user.MyBookingDTO;
import fina.skroflin.model.dto.booking.user.MyBookingResponseDTO;
import fina.skroflin.service.BookingService;
import fina.skroflin.service.TrainingSessionService;
import fina.skroflin.service.user.UserService;
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

@Tag(name = "Booking", description = "Available endpoints for the entity 'Booking'")
@RestController
@RequestMapping("/api/fina/skroflin/booking")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final TrainingSessionService trainingSessionService;

    public BookingController(BookingService bookingService, UserService userService, TrainingSessionService trainingSessionService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.trainingSessionService = trainingSessionService;
    }

    @Operation(
            summary = "Retrieves all bookings", tags = {"get", "booking"},
            description = "Retrieves all bookings with information about"
                    + " " + "their respectful users, their training sessions,"
                    + " " + "their reservation time and when will their"
                    + " " + "reservation end."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity<List<BookingResponseDTO>> getAll() {
        try {
            return new ResponseEntity<>(
                    bookingService.getAll(), HttpStatus.OK
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
            summary = "Retrieves booking by id",
            description = "Retrieves booking by id with its whole respectful data."
                    + " " + "If there is no id for the given booking, no result is retrieved.",
            tags = {"booking", "getBy"},
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of booking in the database."
                                + " " + "Must be greater than 0!",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Booking.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Therer is no booking for the given id."),
        @ApiResponse(responseCode = "400", description = "Id must be greater than 0!", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getById")
    public ResponseEntity<BookingResponseDTO> getById(
            @RequestParam int id
    ) {
        try {
            if (id <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Id musn't be lesser than 0!"
                );
            }
            BookingResponseDTO booking = bookingService.getById(id);
            if (booking == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking with the id" + " " + id + " " + "doesn't exist!"
                );
            }
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon fetching booking with id"
                    + " " + id + " " + e.getMessage(),
                    e
            );
        }
    }
    
    @Operation(
            summary = "Retrieves booking data of the user in the session", tags = {"get", "booking", "getMyBooking"},
            description = "Retrieves data of the current authentiacted user with information about"
                    + " " + "their user, training sesion, reservation time and end of."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/getMyBookings")
    public ResponseEntity<List<MyBookingResponseDTO>> getMyBookings(
            @RequestHeader
            HttpHeaders headers
    ){
        try {
            return ResponseEntity.ok(bookingService.getMyBookings(headers));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon fetching" + " " + e.getMessage(),
                    e
            );
        }
    }

    @Operation(
            summary = "Create new booking",
            tags = {"post", "booking"},
            description = "Create new booking. User id, training session id,"
                    + " " + "reservation time and end of reservation time is necessary!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Booking.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request (dto object wasn't received)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity<BookingResponseDTO> post(
            @RequestBody(required = true) BookingDTO dto
    ) {
        try {
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
                            "Error user with id"
                            + " " + dto.userId()
                            + " " + "doesn't exist!"
                    );
                }
            }
            if (dto.trainingSessionId() != null) {
                try {
                    trainingSessionService.getById(dto.trainingSessionId());
                } catch (Exception e) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Error training session with id"
                            + " " + dto.userId()
                            + " " + "doesn't exist"
                    );
                }
            }
            if (dto.reservationTime() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Reservation time is necessary!"
                );
            }
            if (dto.endOfReservationTime() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "End of reservation time is necessary!"
                );
            }

            BookingResponseDTO createdBooking = bookingService.post(dto);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e
            );
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e
            );
        }
    }
    
    @Operation(
            summary = "Create new booking for the authenticated user",
            tags = {"post", "createMyBooking"},
            description = "Create new booking for the user that is authenticated and"
                    + " " + "currently in the session.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Booking.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request (dto object wasn't received)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/createMyBooking")
    public ResponseEntity<MyBookingResponseDTO> createMyBooking(
            @RequestHeader
            HttpHeaders headers,
            @RequestBody(required = true)
            MyBookingDTO dto
    ){
        try {
            return ResponseEntity.ok(bookingService.createMyBooking(dto, headers));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon creating booking" + " " + e.getMessage(),
                    e
            );
        }
    }

    @Operation(
            summary = "Updates booking", tags = {"put", "booking"},
            description = "Updates booking.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the booking in the database, must be greater than 0!",
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
    public ResponseEntity<BookingResponseDTO> put(
            @RequestParam int id,
            @RequestBody(required = true) BookingDTO dto
    ) {
        try {
            if (id <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Id musn't be lesser than 0"
                );
            }
            if (dto.userId() != null) {
                try {
                    userService.getById(dto.userId());
                } catch (Exception e) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Error user with id"
                            + " " + dto.userId()
                            + " " + "doesn't exist"
                    );
                }
            }
            if (dto.trainingSessionId() != null) {
                try {
                    trainingSessionService.getById(dto.trainingSessionId());
                } catch (Exception e) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Error training session with id"
                            + " " + dto.userId()
                            + " " + "doesn't exist"
                    );
                }
            }
            if (dto.reservationTime() == null
                    || dto.reservationTime().equals(null)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Reservation time is necessary!"
                );
            }
            BookingResponseDTO updatedBooking = bookingService.put(dto, id);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e
            );
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e
            );
        }
    }
    
    @Operation(
            summary = "Updates booking for authenticated user", tags = {"put", "updateMyBooking", "user"},
            description = "Updates booking for user that is currently logged"
                    + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/updateMyBooking")
    public ResponseEntity<MyBookingResponseDTO> updateMyBooking(
            @RequestHeader
            HttpHeaders headers,
            @RequestBody(required = true)
            MyBookingDTO dto,
            int id
    ){
        try {
            return ResponseEntity.ok(
                    bookingService.updateMyBooking(
                            dto, id, headers
                    )
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e
            );
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e
            );
        }
    }
    
    @Operation(
            summary = "Deletes booking", tags = {"delete", "booking"},
            description = "Deletes booking.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the booking in the database, must be greater than 0!",
                        example = "1"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                description = "Updated", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", 
                description = "Bad request (can't delete because there is no booking)", 
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
            bookingService.delete(id);
            return new ResponseEntity<>(
                    "Booking with id" 
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
    
    @Operation(
            summary = "Deletes booking of user in session", tags = {"delete", "deleteMyBooking", "user"},
            description = "Deletes booking for user that is" + " "
                    + "currently logged" + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                description = "Deleted", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", 
                description = "Bad request (can't delete because there is no booking)", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", 
                description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/deleteMyBooking")
    public ResponseEntity<String> deleteMyBooking(
            int id,
            @RequestHeader
            HttpHeaders headers
    ){
        try {
            return ResponseEntity.ok(
                    bookingService.deleteMyBooking(
                            id, headers
                    )
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
