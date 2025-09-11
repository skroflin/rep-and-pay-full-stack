/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.training.user;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record UserTrainingSessionResponseDTO(
        Integer id,
        @Enumerated(EnumType.STRING)
        TrainingType trainingType,
        @Enumerated(EnumType.STRING)
        TrainingLevel trainingLevel,
        String trainerFirstName,
        String trainerLastName,
        LocalDateTime beginningOfSession,
        LocalDateTime endOfSession
        ) {
    
}
