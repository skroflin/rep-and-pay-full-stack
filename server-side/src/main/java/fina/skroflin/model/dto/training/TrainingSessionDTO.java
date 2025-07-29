/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.training;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record TrainingSessionDTO(
        @Schema(example = "1")
        Integer trainerId,
        @Schema(example = "2025-07-29T20:00:00")
        LocalDateTime dateTime,
        @Schema(example = "weightlifting")
        TrainingType trainingType,
        @Schema(example = "intermediate")
        TrainingLevel trainingLevel,
        @Schema(example = "20")
        int capacity
        ) {
    
}
