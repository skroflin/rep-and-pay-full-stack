/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.booking.BookingDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.booking.user.MyBookingDTO;
import fina.skroflin.model.dto.booking.user.MyBookingResponseDTO;
import fina.skroflin.service.BookingService;
import fina.skroflin.service.TrainingSessionService;
import fina.skroflin.service.user.UserService;
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
