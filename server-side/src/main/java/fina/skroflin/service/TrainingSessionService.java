/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.training.user.MyTrainingSessionRequestDTO;
import fina.skroflin.model.dto.training.user.MyTrainingSessionResponseDTO;
import fina.skroflin.model.dto.training.TrainingSessionRequestDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import fina.skroflin.model.dto.training.user.UserTrainingSessionResponseDTO;
import fina.skroflin.model.enums.TrainingLevel;
import fina.skroflin.model.enums.TrainingType;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class TrainingSessionService extends MainService {

    private final JwtTokenUtil jwtTokenUtil;

    public TrainingSessionService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public TrainingSessionResponseDTO convertToResponseDTO(
            TrainingSession trainingSession) {

        if (trainingSession == null) {
            return null;
        }

        return new TrainingSessionResponseDTO(
                trainingSession.getId(),
                trainingSession.getTrainer().getFirstName(),
                trainingSession.getTrainer().getLastName(),
                trainingSession.getTrainingType(),
                trainingSession.getTrainingLevel(),
                trainingSession.getBeginningOfSession(),
                trainingSession.getEndOfSession(),
                trainingSession.isAlreadyBooked()
        );
    }

    @Transactional
    public MyTrainingSessionResponseDTO converToMyResponseDTO(
            TrainingSession trainingSession
    ) {
        if (trainingSession == null) {
            return null;
        }

        return new MyTrainingSessionResponseDTO(
                trainingSession.getId(),
                trainingSession.getTrainingType(),
                trainingSession.getTrainingLevel(),
                trainingSession.getBeginningOfSession(),
                trainingSession.getEndOfSession(),
                trainingSession.isAlreadyBooked()
        );
    }

    @Transactional
    public UserTrainingSessionResponseDTO convertToUserResponseDTO(
            Booking booking
    ) {
        if (booking.getTrainingSession() == null) {
            return null;
        }

        return new UserTrainingSessionResponseDTO(
                booking.getTrainingSession().getId(),
                booking.getTrainingSession().getTrainingType(),
                booking.getTrainingSession().getTrainingLevel(),
                booking.getTrainingSession().getTrainer().getFirstName(),
                booking.getTrainingSession().getTrainer().getLastName(),
                booking.getTrainingSession().getBeginningOfSession(),
                booking.getTrainingSession().getEndOfSession()
        );
    }

    public List<TrainingSessionResponseDTO> getAll() {
        try {
            List<TrainingSession> trainingSessions = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "left join fetch ts.trainer", TrainingSession.class
            )
                    .list();
            return trainingSessions.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching training sessions:"
                    + " " + e.getMessage(), e);
        }
    }

    public TrainingSessionResponseDTO
            getById(int id) {
        try {
            TrainingSession trainingSession = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "left join fetch ts.trainer "
                    + "where ts.id = :id", TrainingSession.class
            )
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

    public List<MyTrainingSessionResponseDTO> getMyTrainingSessions(
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));
            List<TrainingSession> trainingSessions = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "left join fetch ts.trainer "
                    + "where ts.trainer.id = :userId",
                    TrainingSession.class
            )
                    .setParameter("userId", userId)
                    .list();
            return trainingSessions.stream()
                    .map(this::converToMyResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching training sessions:"
                    + " " + e.getMessage(), e);
        }
    }

    public List<UserTrainingSessionResponseDTO> getUserTrainingSessions(
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));
            List<Booking> bookings = session.createQuery(
                    "select b from Booking b "
                    + "left join fetch b.user u "
                    + "left join fetch b.trainingSession ts "
                    + "where u.id = :userId "
                    + "and b.bookingStatus = accepted", Booking.class
            )
                    .setParameter("userId", userId)
                    .list();

            return bookings.stream()
                    .map(this::convertToUserResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching user training sessions:"
                    + " " + e.getMessage(), e);
        }
    }

    public void post(TrainingSessionRequestDTO o) {
        try {
            User trainer = (User) session.get(User.class,
                     o.trainerId());
            if (trainer == null || !trainer.equals(trainer.getRole())) {
                throw new IllegalArgumentException(
                        "Trainer with the id" + " "
                        + o.trainerId() + " "
                        + "doesn't exist or is not a trainer!");
            }

            Long count = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.beginningOfSession = :reservation "
                    + "and ts.endOfSession = :reservation",
                    Long.class
            )
                    .setParameter("trainerId", o.trainerId())
                    .setParameter("reservation", o.beginningOfSession())
                    .setParameter("reservation", o.endOfSession())
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + o.trainerId() + " " + "already has a session"
                        + " " + "at that time!");
            }

            TrainingSession ts = new TrainingSession(
                    trainer,
                    o.trainingType(),
                    o.trainingLevel(),
                    o.beginningOfSession(),
                    o.endOfSession(),
                    o.alreadyBooked()
                    
            );
            session.beginTransaction();
            session.persist(ts);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon training session "
                    + "booking:" + e.getMessage(), e);
        }
    }

    public void createMyTrainingSession(
            MyTrainingSessionRequestDTO o,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User trainerProfile = (User) session.get(User.class,
                     userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }

            Long count = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.beginningOfSession = :reservation",
                    Long.class
            )
                    .setParameter("trainerId", userId)
                    .setParameter("reservation", o.beginningOfSession())
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + userId + " " + "already has a session"
                        + " " + "at that time!");
            }

            Long count2 = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.endOfSession = :reservation",
                    Long.class
            )
                    .setParameter("trainerId", userId)
                    .setParameter("reservation", o.beginningOfSession())
                    .uniqueResult();

            if (count2 > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + userId + " " + "already has a session"
                        + " " + "that is ending at that time!");
            }

            TrainingSession trainingSession = new TrainingSession(
                    trainerProfile,
                    o.trainingType(),
                    o.trainingLevel(),
                    o.beginningOfSession(),
                    o.endOfSession(),
                    o.alreadyBooked()
            );

            // trainingSession.setAlreadyBooked(false);
            session.beginTransaction();
            session.persist(trainingSession);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon creating training session:"
                    + e.getMessage(), e);
        }
    }

    public void updateMyTrainingSession(
            MyTrainingSessionRequestDTO o,
            int id,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            TrainingSession existingSession = session.get(TrainingSession.class,
                     id);
            if (existingSession == null) {
                throw new NoResultException("Training session with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            if (!existingSession.getTrainer().equals(userId)) {
                throw new SecurityException("You are not authorized to"
                        + " " + "update this training sesion!");
            }

            Long count = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.dateTime = :reservation",
                    Long.class
            )
                    .setParameter("trainerId", userId)
                    .setParameter("reservation", o.beginningOfSession())
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + userId + " " + "already has a session"
                        + " " + "at that time!");
            }

            Long count2 = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.beginningOfSession = :reservation",
                    Long.class
            )
                    .setParameter("trainerId", userId)
                    .setParameter("reservation", o.beginningOfSession())
                    .uniqueResult();

            if (count2 > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + userId + " " + "already has a session"
                        + " " + "that is ending at that time!");
            }

            existingSession.setTrainingType(o.trainingType());
            existingSession.setTrainingLevel(o.trainingLevel());
            existingSession.setBeginningOfSession(o.beginningOfSession());
            existingSession.setEndOfSession(o.endOfSession());

            session.beginTransaction();
            session.persist(existingSession);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon creating training session:"
                    + e.getMessage(), e);
        }
    }

    public void put(TrainingSessionRequestDTO o, int id) {
        try {
            TrainingSession existingSession
                    = (TrainingSession) session.get(TrainingSession.class,
                             id);

            if (existingSession == null) {
                throw new NoResultException("Training session with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            User trainer = (User) session.get(User.class,
                     o.trainerId());
            if (trainer == null || !"trainer".equals(trainer.getRole())) {
                throw new NoResultException("Trainer with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            Long count = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.beginningOfSession = :reservation "
                    + "and ts.id <> :id", Long.class
            )
                    .setParameter("trainerId", o.trainerId())
                    .setParameter(":reservation", o.beginningOfSession())
                    .setParameter(":id", id)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + o.trainerId() + " " + "already has a session"
                        + " " + "at that time!");
            }

            Long count2 = session.createQuery(
                    "select count(ts) from TrainingSession ts "
                    + "where ts.trainer.id = :trainerId "
                    + "and ts.endOfSession = :reservation "
                    + "and ts.id <> :id", Long.class
            )
                    .setParameter("trainerId", o.trainerId())
                    .setParameter(":reservation", o.endOfSession())
                    .setParameter(":id", id)
                    .uniqueResult();

            if (count2 > 0) {
                throw new IllegalArgumentException("Trainer with the id"
                        + " " + o.trainerId() + " " + "already has a session"
                        + " " + "that is ending at that time!");
            }

            existingSession.setTrainer(trainer);
            existingSession.setTrainingType(o.trainingType());
            existingSession.setTrainingLevel(o.trainingLevel());
            existingSession.setBeginningOfSession(o.beginningOfSession());
            existingSession.setEndOfSession(o.endOfSession());

            session.beginTransaction();
            session.merge(existingSession);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon updating training session"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }

    public String
            delete(int id) {
        try {
            TrainingSession trainingSession
                    = (TrainingSession) session.get(TrainingSession.class,
                             id);
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

    public String deleteMyTrainingSession(int id, HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            TrainingSession trainingSession
                    = (TrainingSession) session.get(TrainingSession.class,
                             id);

            if (trainingSession == null) {
                throw new NoResultException("Training session with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            if (!trainingSession.getTrainer().getId().equals(userId)) {
                throw new SecurityException("You are not authorized to"
                        + " " + "delete this training session!");
            }

            session.beginTransaction();
            session.remove(trainingSession);
            session.getTransaction().commit();

            return "Training session with id" + " " + id + " " + "deleted!";
        } catch (Exception e) {
            throw new RuntimeException("Error upon deleting training session"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }

    public List<TrainingSessionResponseDTO> getAvailableTrainingSessionsByDate(
            LocalDate date
    ) {
        try {
            List<TrainingSession> trainingSessions = session.createQuery(
                    "select ts from TrainingSession ts "
                    + "where date(ts.beginningOfSession) = :date",
                    TrainingSession.class)
                    .setParameter("date", date)
                    .list();
            return trainingSessions.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching training sessions:"
                    + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfMyCoachTrainingSessions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User trainerProfile = (User) session.get(User.class,
                     userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }

            Long numOfMyTrainingSessions = session.createQuery(
                    "select count(ts.id) from TrainingSession ts "
                    + "left join ts.trainer "
                    + "where ts.trainer.id = :userId",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .getSingleResult();
            return numOfMyTrainingSessions;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of training sessions"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfMyUserTrainingSessions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User userProfile = (User) session.get(User.class,
                     userId);
            if (userProfile == null) {
                throw new NoResultException("User not found");
            }

            Long numOfMyTrainingSessions = session.createQuery(
                    "select count(ts.id) from Booking b "
                    + "left join b.trainingSession ts "
                    + "left join b.user u "
                    + "where u.id = :userId "
                    + "and b.bookingStatus = accepted",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .getSingleResult();
            return numOfMyTrainingSessions;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of training sessions"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfBeginnerTrainingSessions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User trainerProfile = (User) session.get(User.class,
                     userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }

            Long numOfMyTrainingSessions = session.createQuery(
                    "select count(ts.id) from TrainingSession ts "
                    + "left join ts.trainer "
                    + "where ts.trainer.id = :userId "
                    + "and ts.trainingLevel = beginner",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .getSingleResult();
            return numOfMyTrainingSessions;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of training sessions"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfIntermediateTrainingSessions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User trainerProfile = (User) session.get(User.class,
                     userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }

            Long numOfMyTrainingSessions = session.createQuery(
                    "select count(ts.id) from TrainingSession ts "
                    + "left join ts.trainer "
                    + "where ts.trainer.id = :userId "
                    + "and ts.trainingLevel = intermediate",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .getSingleResult();
            return numOfMyTrainingSessions;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of training sessions"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfAdvancedTrainingSessions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class
                    ));

            User trainerProfile = (User) session.get(User.class,
                     userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }

            Long numOfMyTrainingSessions = session.createQuery(
                    "select count(ts.id) from TrainingSession ts "
                    + "left join ts.trainer "
                    + "where ts.trainer.id = :userId "
                    + "and ts.trainingLevel = advanced",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .getSingleResult();
            return numOfMyTrainingSessions;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of training sessions"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }
}
