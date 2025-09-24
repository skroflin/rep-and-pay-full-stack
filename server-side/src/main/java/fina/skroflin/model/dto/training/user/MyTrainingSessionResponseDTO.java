/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.training.user;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record MyTrainingSessionResponseDTO(
        Integer id,
        @Enumerated(EnumType.STRING)
        TrainingType trainingType,
        @Enumerated(EnumType.STRING)
        TrainingLevel trainingLevel,
        LocalDateTime beginningOfSession,
        LocalDateTime endOfSession,
        boolean alreadyBooked,
        boolean bookedByMe
        ) {    
}
