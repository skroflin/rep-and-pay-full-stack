/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.Users;
import fina.skroflin.model.dto.training.TrainingSessionDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class TrainingSessionService extends MainService {

    @Transactional
    public TrainingSessionResponseDTO convertToResponseDTO(
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
    private TrainingSession convertToEntity(TrainingSessionDTO dto) {
        TrainingSession trainingSession = new TrainingSession();
        if (dto.trainerId() != null) {
            Users trainer = (Users) session.get(Users.class, dto.trainerId());
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
            TrainingSessionDTO dto) {
        if (dto.trainerId() != null) {
            Users trainer = (Users) session.get(Users.class, dto.trainerId());
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

    public List<TrainingSessionResponseDTO> getAll() {
        try {
            List<TrainingSession> trainingSessions = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "left join fetch ts.trainer", TrainingSession.class)
                    .list();
            return trainingSessions.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching training sessions:"
                    + " " + e.getMessage(), e);
        }
    }

    public TrainingSessionResponseDTO getById(int id) {
        try {
            TrainingSession trainingSession = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "left join fetch ts.trainer "
                    + "where ts.id = :id", TrainingSession.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (trainingSession == null) {
                throw new NoResultException("Training session with id"
                        + " " + id + " " + "doesn't exist!");
            }

            return convertToResponseDTO(trainingSession);
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching "
                    + "training session with id"
                    + " " + id + ": " + e.getMessage(), e);
        }
    }

    public TrainingSessionResponseDTO post(TrainingSessionDTO o) {
        try {
            Users trainer = (Users) 
                    session.get(Users.class, o.trainerId());
            if (trainer == null || !"trainer".equals(trainer.getRole())) {
                throw new IllegalArgumentException(
                        "Trainer with the id" + " "
                        + o.trainerId() + " "
                        + "doesn't exist or is not a trainer!");
            }

            TrainingSession ts = convertToEntity(o);
            session.beginTransaction();
            session.persist(ts);
            session.getTransaction().commit();

            return convertToResponseDTO(ts);
        } catch (Exception e) {
            throw new RuntimeException("Error upon training session "
                    + "booking:" + e.getMessage(), e);
        }
    }

    public TrainingSessionResponseDTO put(TrainingSessionDTO o, int id) {
        try {
            TrainingSession existingSession
                    = (TrainingSession) session.get(TrainingSession.class, id);

            if (existingSession == null) {
                throw new NoResultException("Training session with the id"
                        + " " + id + " " + "doesn't exist!");
            }
            
            Users trainer = (Users) 
                    session.get(Users.class, o.trainerId());
            if (trainer == null || !"trainer".equals(trainer.getRole())) {
                throw new NoResultException("Trainer with the id"
                        + " " + id + " " + "doesn't exist!");
            }
            
            Long count = session.createQuery(
                    "select count(ts) from TrainingSession ts"
                            + "where ts.trainer.id = :trainerId"
                            + "and ts.dateTime = :reservation"
                            + "and ts.id <> :id", Long.class)
                    .setParameter("trainerId", o.trainerId())
                    .setParameter(":reservation", o.dateTime())
                    .setParameter(":id", id)
                    .uniqueResult();
            
            if (count > 0) {
                throw new IllegalArgumentException("Trainer with the id" 
                        + " " + o.trainerId() + " " + "already has a session"
                                + " " + "at that time!");
            }
            
            updateEntityFromDto(existingSession, o);
            session.beginTransaction();
            session.merge(existingSession);
            session.getTransaction().commit();
            return convertToResponseDTO(existingSession);
        } catch (Exception e) {
            throw new RuntimeException("Error upon updating training session"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }
    
    public String delete(int id){
        try {
            TrainingSession trainingSession =
                    (TrainingSession) session.get(TrainingSession.class, id);
            if (trainingSession == null) {
                throw new NoResultException("Training sesion with id"
                        + " " + id + " " + "doesn't exist!");
            }
            session.remove(trainingSession);
            return "Training session with id" + " " + id + " " + "deleted!";
        } catch (Exception e) {
            throw new RuntimeException("Error upon deleting training session"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }
}
