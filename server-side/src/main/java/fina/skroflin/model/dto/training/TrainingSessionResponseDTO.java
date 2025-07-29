/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package fina.skroflin.model.dto.training;

import fina.skroflin.model.User;
import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
public record TrainingSessionResponseDTO(
        Integer id,
        User trainer,
        LocalDateTime dateTime,
        TrainingType trainingType,
        TrainingLevel trainingLevel,
        int capacity
        ) {

}
