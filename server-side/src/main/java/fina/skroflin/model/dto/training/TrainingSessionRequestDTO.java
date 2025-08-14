/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model.dto.training;

import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record TrainingSessionRequestDTO(
        @Schema(example = "1")
        Integer trainerId,
        @Schema(example = "weightlifting")
        TrainingType trainingType,
        @Schema(example = "intermediate")
        TrainingLevel trainingLevel,
        @Schema(example = "2025-08-14T18:00")
        LocalDateTime beginningOfSession,
        @Schema(example = "2025-08-14T19:30")
        LocalDateTime endOfSession
        ) {
    
}
