/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.dto.training.TrainingSessionRequestDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import fina.skroflin.model.dto.training.user.MyTrainingSessionRequestDTO;
import fina.skroflin.model.dto.training.user.MyTrainingSessionResponseDTO;
import fina.skroflin.service.TrainingSessionService;
import fina.skroflin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@Tag(name = "TrainingSession", description = "Available endpoints for the entity 'TrainingSession'")
@RestController
@RequestMapping("/api/fina/skroflin/trainingSession")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;
    private final UserService trainerService;

    public TrainingSessionController(
            TrainingSessionService trainingSessionService,
            UserService trainerService
    ) {
        this.trainingSessionService = trainingSessionService;
        this.trainerService = trainerService;
    }

    @Operation(
            summary = "Retrieves all training sessions", tags = {"get", "trainingSessions"},
            description = "Retrieves all bookings with information about"
                    + " " + "their respectful users, date time"
                    + " " + "training type, training level and capacity of the session."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainingSession.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity<List<TrainingSessionResponseDTO>> getAll() {
        return new ResponseEntity<>(
                trainingSessionService.getAll(), HttpStatus.OK
        );
    }

    @Operation(
            summary = "Retrieves training session by id",
            description = "Retrieves training session by id with its whole respectful data."
                    + " " + "If there is no id for the given training session, no result is retrieved.",
            tags = {"trainingSession", "getBy"},
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of training session in the database."
                                + " " + "Must be greater than 0!",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Booking.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "There is no training session for the given id."),
        @ApiResponse(responseCode = "400", description = "Id must be greater than 0!", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getById")
    public ResponseEntity<TrainingSessionResponseDTO> getById(
            @RequestParam int id
    ) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id musn't be lesser than 0!"
            );
        }
        TrainingSessionResponseDTO trainingSession
                = trainingSessionService.getById(id);
        if (trainingSession == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Training session with id" + " "
                    + id + " " + "doesn't exist!"
            );
        }
        return new ResponseEntity<>(trainingSession, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieves training session data of the user in the session", tags = {"get", "trainingSession", "getMyTrainingSession"},
            description = "Retrieves data of the current authentiacted user with information about"
                    + " " + "their trainer, date time, training type, training level and capacity."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/getMyTrainingSessions")
    public ResponseEntity<List<MyTrainingSessionResponseDTO>> getMyTrainingSessions(
            @RequestHeader HttpHeaders headers
    ) {
        List<MyTrainingSessionResponseDTO> myTrainingSessions = 
                trainingSessionService.getMyTrainingSessions(headers);
        return new ResponseEntity<>(myTrainingSessions, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Create new training session",
            tags = {"post", "trainingSesion"},
            description = "Create new training session. User id, date time,"
                    + " " + "training type, training level and capacity is necessary!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Booking.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request (dto object wasn't received)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity<String> post(
            @RequestBody(required = true) TrainingSessionRequestDTO dto
    ) {
        if (dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The necessary data wasn't inserted!"
            );
        }
        if (dto.trainerId() != null) {
            try {
                trainerService.getById(dto.trainerId());
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Error trainer with it"
                        + " " + dto.trainerId()
                        + " " + "doesn't exist!"
                );
            }
        }
        if (dto.dateTime() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Date time is necessary!"
            );
        }
        if (dto.trainingLevel() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Training level is necessary!"
            );
        }
        if (dto.trainingType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Training type is necessary!"
            );
        }
        trainingSessionService.post(dto);
        return new ResponseEntity<>(
                "Training session created!",
                HttpStatus.CREATED
        );
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
    @PostMapping("/createMyTrainingSession")
    public ResponseEntity<String> createMyTrainingSession(
            @RequestHeader HttpHeaders headers,
            MyTrainingSessionRequestDTO dto
    ) {
        trainingSessionService.createMyTrainingSession(dto, headers);
        return new ResponseEntity<>("New session created!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Updates training session", tags = {"put", "trainingSession"},
            description = "Updates training session.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the trainingSession in the database, must be greater than 0!",
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
    public ResponseEntity<String> put(
            @RequestParam int id,
            @RequestBody(required = true) TrainingSessionRequestDTO dto
    ) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id musn't be lesser than 0!"
            );
        }
        if (dto.dateTime() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Date time is necessary!"
            );
        }
        if (dto.trainingLevel() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Training level is necessary!"
            );
        }
        if (dto.trainingType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Training type is necessary!"
            );
        }
        trainingSessionService.put(dto, id);
        return new ResponseEntity<>(
                "Training session with the id" 
                        + " " + id + " " + "updated!",
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Updates training session for authenticated user", tags = {"put", "updateMyBooking", "trainingSession"},
            description = "Updates training session for user that is currently logged"
                    + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/updateMyTrainingSession")
    public ResponseEntity<String> updateMyTrainingSession(
            @RequestHeader HttpHeaders headers,
            MyTrainingSessionRequestDTO dto,
            int id
    ) {
        trainingSessionService.updateMyTrainingSession(dto, id, headers);
        return new ResponseEntity<>(
                "My training session with id" 
                        + " " + id + " " + "updated!", 
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Deletes training session", tags = {"delete", "trainingSession"},
            description = "Deletes training session.",
            parameters = {
                @Parameter(
                        name = "id",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primary key of the trainingSession in the database, must be greater than 0!",
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
    ) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id musn't be lesser than 0!"
            );
        }
        trainingSessionService.delete(id);
        return new ResponseEntity<>(
                "Training session with id"
                + " " + id
                + " " + "deleted",
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Deletes training session of user in session", 
            tags = {"delete", "deleteMyBooking", "trainingSession"},
            description = "Deletes trainingSession for user that is" + " "
                    + "currently logged" + " " + "in the session."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                description = "Deleted", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", 
                description = "Bad request (can't delete because there is no trainingSession)", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", 
                description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/deleteMyTrainingSession")
    public ResponseEntity<String> deleteMyTrainingSession(
            @RequestHeader HttpHeaders headers,
            int id
    ) {
        trainingSessionService.deleteMyTrainingSession(id, headers);
        return new ResponseEntity<>(
                "My training session with id" 
                        + " " + id + " " + "deleted!", 
                HttpStatus.OK
        );
    }
    
    @GetMapping("/getAvailableTrainingSessionsByDate")
    public ResponseEntity<List<TrainingSessionResponseDTO>> getAvailableSessions(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)        
            LocalDate date
    ){
        List<TrainingSessionResponseDTO> availableSessionsByDate
                = trainingSessionService.getAvailableTrainingSessionsByDate(date);
        return new ResponseEntity<>(availableSessionsByDate, HttpStatus.OK);
    }
}
