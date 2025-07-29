/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.training;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record TrainingSessionResponseDTO(
        Integer id,
        Integer trainerId,
        LocalDateTime dateTime,
        @Enumerated(EnumType.STRING)
        TrainingType trainingType,
        @Enumerated(EnumType.STRING)
        TrainingLevel trainingLevel,
        int capacity
        ) {

}
