/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.training.TrainingSessionDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import jakarta.transaction.Transactional;

/**
 *
 * @author skroflin
 */
public class TrainingSessionService extends MainService {
    
    @Transactional
    private TrainingSessionResponseDTO convertToResponseDTO(
            TrainingSession trainingSession) {
        
        if (trainingSession == null) {
            return null;
        }
        
        Integer trainerId = (trainingSession.getId() != null) 
                ? trainingSession.getTrainer().getId() : null;
        
        return new TrainingSessionResponseDTO(
                trainingSession.getId(),
                trainerId, 
                trainingSession.getDateTime(),
                trainingSession.getTrainingType(),
                trainingSession.getTrainingLevel(),
                trainingSession.getCapacity()
        );
    }
    
    @Transactional
    private TrainingSession convertToEntity(TrainingSessionDTO dto){
        TrainingSession trainingSession = new TrainingSession();
        if (dto.trainerId() != null) {
            User trainer = (User) session.get(User.class, dto.trainerId());
            if (trainer == null) {
                throw new IllegalArgumentException("Trainer with the id" 
                        + " " + dto.trainerId() + " " + "doesn't exist!");
            }
            trainingSession.setTrainer(trainer);
        }
        trainingSession.setDateTime(dto.dateTime());
        trainingSession.setTrainingType(dto.trainingType());
        trainingSession.setTrainingLevel(dto.trainingLevel());
        trainingSession.setCapacity(dto.capacity());
        return trainingSession;
    }
    
    @Transactional
    private void updateEntityFromDto(
            TrainingSession trainingSession, 
            TrainingSessionDTO dto){
        if (dto.trainerId() != null) {
            User trainer = (User) session.get(User.class, dto.trainerId());
            if (trainer == null) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + dto.trainerId() + " " + "doesn't exist!");
            }
            trainingSession.setTrainer(trainer);
        } else {
            trainingSession.setTrainer(null);
        }
        trainingSession.setDateTime(dto.dateTime());
        trainingSession.setTrainingType(dto.trainingType());
        trainingSession.setTrainingLevel(dto.trainingLevel());
        trainingSession.setCapacity(dto.capacity());
    }
}
