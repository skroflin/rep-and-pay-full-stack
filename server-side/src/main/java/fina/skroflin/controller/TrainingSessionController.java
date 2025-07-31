/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.controller;

import fina.skroflin.model.dto.training.TrainingSessionDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import fina.skroflin.service.TrainingSessionService;
import fina.skroflin.service.user.UserService;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author skroflin
 */
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

    @GetMapping("/get")
    public ResponseEntity<List<TrainingSessionResponseDTO>> getAll() {
        try {
            return new ResponseEntity<>(
                    trainingSessionService.getAll(), HttpStatus.OK
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
    public ResponseEntity<TrainingSessionResponseDTO> getById(
            @RequestParam int id
    ) {
        try {
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
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error upon fetching training session with id"
                    + " " + id + " " + e.getMessage(),
                    e
            );
        }
    }

    @PostMapping("/post")
    public ResponseEntity<TrainingSessionResponseDTO> post(
            @RequestBody(required = true) TrainingSessionDTO dto
    ) {
        try {
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
            if (dto.capacity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Capacity is necessary and it "
                        + "cannot be lesser than "
                        + "or equal to zero!"
                );
            }

            TrainingSessionResponseDTO createdTrainingSession
                    = trainingSessionService.post(dto);
            return new ResponseEntity<>(createdTrainingSession, HttpStatus.CREATED);
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

    @PutMapping("/put")
    public ResponseEntity<TrainingSessionResponseDTO> put(
            @RequestParam int id,
            @RequestBody(required = true) TrainingSessionDTO dto
    ) {
        try {
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
            if (dto.capacity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Capacity is necessary and it "
                        + "cannot be lesser than "
                        + "or equal to zero!"
                );
            }
            TrainingSessionResponseDTO updatedTrainingSession = 
                    trainingSessionService.put(dto, id);
            return new ResponseEntity<>(updatedTrainingSession, HttpStatus.OK);
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
            trainingSessionService.delete(id);
            return new ResponseEntity<>(
                    "Training session with id" 
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
