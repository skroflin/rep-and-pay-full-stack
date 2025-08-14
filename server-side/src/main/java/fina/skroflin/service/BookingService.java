/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.booking.BookingRequestDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.booking.user.MyBookingRequestDTO;
import fina.skroflin.model.dto.booking.user.MyBookingResponseDTO;
import fina.skroflin.model.dto.booking.user.TrainerBookingResponseDTO;
import fina.skroflin.model.enums.BookingStatus;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class BookingService extends MainService {

    private final JwtTokenUtil jwtTokenUtil;

    public BookingService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public BookingResponseDTO convertToResponseDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        Integer trainingSessionId = (booking.getId() != null)
                ? booking.getTrainingSession().getId() : null;

        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getFirstName(),
                booking.getUser().getLastName(),
                trainingSessionId,
                booking.getBookingStatus()
        );
    }

    @Transactional
    public MyBookingResponseDTO convertToMyResponseDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        Integer trainingSessionId = (booking.getId() != null)
                ? booking.getTrainingSession().getId() : null;
        return new MyBookingResponseDTO(
                booking.getId(),
                trainingSessionId,
                booking.getBookingStatus()
        );
    }
    
    public TrainerBookingResponseDTO convertToTrainerBookingResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        Integer trainingSessionId = (booking.getId() != null)
                ? booking.getTrainingSession().getId() : null;
        return new TrainerBookingResponseDTO(
                booking.getId(), 
                trainingSessionId, 
                booking.getUser().getFirstName(), 
                booking.getUser().getLastName(), 
                booking.getTrainingSession().getTrainingType(), 
                booking.getTrainingSession().getBeginningOfSession(), 
                booking.getTrainingSession().getEndOfSession(), 
                booking.getBookingStatus()
        );
    }

    public List<BookingResponseDTO> getAll() {
        try {
            List<Booking> bookings = session.createQuery(
                    "select b from Booking b "
                    + "left join fetch b.user "
                    + "left join fetch b.trainingSession", 
                    Booking.class).list();
            return bookings.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching bookings:"
                    + " " + e.getMessage(), e);
        }
    }

    public BookingResponseDTO getById(int id) {
        try {
            Booking booking = session.createQuery(
                    "select b from Booking b "
                    + "left join fetch b.user "
                    + "left join fetch b.trainingSession "
                    + ""
                    + "where b.id = :id", Booking.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (booking == null) {
                throw new NoResultException("Booking with id"
                        + " " + id + " " + "doesn't exist!");
            }

            return convertToResponseDTO(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching "
                    + "booking with id"
                    + " " + id + ": " + e.getMessage(), e);
        }
    }

    public List<MyBookingResponseDTO> getMyBookings(
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            List<Booking> bookings = session.createQuery(
                    "select b from Booking b "
                    + "left join fetch b.user "
                    + "left join fetch b.trainingSession "
                    + "where b.user.id = :userId",
                    Booking.class)
                    .setParameter("userId", userId)
                    .list();
            return bookings.stream()
                    .map(this::convertToMyResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching bookings:"
                    + " " + e.getMessage(), e);
        }
    }

    public void post(BookingRequestDTO o) {
        try {
            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.user.id = :userId"
                    + "and b.trainingSession.id = :trainingSessionId ", 
                    Long.class)
                    .setParameter("userId", o.userId())
                    .setParameter("trainingSessionId", o.trainingSessionId())
                    .uniqueResult();
            if (count > 0) {
                throw new IllegalArgumentException("User with id"
                        + " " + o.userId() + " " + "already booked this session");
            }
            
            User user = session.get(User.class, o.userId());
            TrainingSession trainingSession = 
                    session.get(TrainingSession.class, o.trainingSessionId());

            Booking booking = new Booking(
                    user,
                    trainingSession,
                    o.bookingStatus()
            );
            session.beginTransaction();
            session.persist(booking);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon creating booking:"
                    + e.getMessage(), e);
        }
    }

    public void createMyBooking(
            MyBookingRequestDTO o,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            User userProfile = (User) session.get(User.class, userId);
            if (userProfile == null) {
                throw new NoResultException("User not found!");
            }

            TrainingSession ts = (TrainingSession) session.get(
                    TrainingSession.class,
                    o.trainingSessionId()
            );
            if (ts == null) {
                throw new NoResultException(
                        "Training session with id"
                        + " " + o.trainingSessionId()
                        + "not found!"
                );
            }
            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.user.id = :userId"
                    + "and b.trainingSession.id = :trainingSessionId ", 
                    Long.class)
                    .setParameter("userId", userId)
                    .setParameter("trainingSessionId", o.trainingSessionId())
                    .uniqueResult();
            if (count > 0) {
                throw new IllegalArgumentException("You have already booked this"
                        + " " + "session!");
            }

            Booking booking = new Booking(
                    userProfile,
                    ts,
                    o.bookingStatus()
            );

            session.beginTransaction();
            session.persist(booking);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon creating booking:"
                    + e.getMessage(), e);
        }
    }

    public void put(BookingRequestDTO o, int id) {
        try {
            Booking existingBooking
                    = (Booking) session.get(Booking.class, id);
            if (existingBooking == null) {
                throw new NoResultException("Booking with id" + " "
                        + id + " " + "doesn't exist!");
            }

            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.user.id = :userId "
                    + "and b.id != :currentId ",
                    Long.class)
                    .setParameter("userId", o.userId())
                    .setParameter("currentId", id)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("You already have a booking"
                        + " " + "that overlaps with this time!");
            }

            User user
                    = (User) session.get(User.class, o.userId());
            if (user == null) {
                throw new NoResultException("User with id" + " "
                        + o.userId() + " " + "doesn't exist!");
            }

            TrainingSession trainingSession
                    = (TrainingSession) session.get(TrainingSession.class, o.trainingSessionId());
            if (trainingSession == null) {
                throw new NoResultException("Training session with id" + " "
                        + o.trainingSessionId() + " " + "doesn't exist!");
            }

            existingBooking.setUser(user);
            existingBooking.setTrainingSession(trainingSession);
            existingBooking.setBookingStatus(o.bookingStatus());
           
            session.beginTransaction();
            session.merge(existingBooking);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error upon updating booking with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public void updateMyBooking(
            MyBookingRequestDTO o,
            int id,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            Booking existingBooking
                    = (Booking) session.get(Booking.class, id);
            if (existingBooking == null) {
                throw new NoResultException("Booking with id" + " "
                        + id + " " + "doesn't exist!");
            }

            if (!existingBooking.getUser().getId().equals(userId)) {
                throw new SecurityException("You are not authorized to"
                        + " " + "update this booking!");
            }

            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.user.id = :userId "
                    + "and b.id != :currentId ",
                    Long.class)
                    .setParameter("userId", userId)
                    .setParameter("currentId", id)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("You already have a booking"
                        + " " + "that overlaps with this time!");
            }

            TrainingSession trainingSession
                    = (TrainingSession) session.get(TrainingSession.class, o.trainingSessionId());
            if (trainingSession == null) {
                throw new NoResultException("Training session with id" + " "
                        + o.trainingSessionId() + " " + "doesn't exist!");
            }

            existingBooking.setTrainingSession(trainingSession);

            session.beginTransaction();
            session.merge(existingBooking);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon updating booking with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public String delete(int id) {
        try {
            Booking booking = (Booking) session.get(Booking.class, id);
            if (booking == null) {
                throw new NoResultException("Booking with id"
                        + " " + id + " " + "doesn't exist!");
            }
            session.remove(booking);
            return "Booking with id" + " " + id + " " + "deleted!";
        } catch (Exception e) {
            throw new RuntimeException("Error upon deleting booking with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public String deleteMyBooking(int id, HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            Booking booking = (Booking) session.get(Booking.class, id);

            if (booking == null) {
                throw new NoResultException("Booking with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            if (!booking.getUser().getId().equals(userId)) {
                throw new SecurityException("You are not authorized to"
                        + " " + "delete this booking!");
            }

            session.beginTransaction();
            session.remove(booking);
            session.getTransaction().commit();

            return "Booking with id" + " " + id + " " + "deleted!";
        } catch (Exception e) {
            throw new RuntimeException("Error upon deleting booking"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }

    public void updateBookingStatus(
            int id,
            BookingStatus newBookingStatus,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            Booking booking = (Booking) session.get(Booking.class, id);

            if (booking == null) {
                throw new NoResultException("Booking with the id"
                        + " " + id + " " + "doesn't exist!");
            }

            Integer trainerId = booking.getTrainingSession().getTrainer().getId();
            if (!trainerId.equals(userId)) {
                throw new SecurityException("You are not authorized to"
                        + " " + "delete this booking!");
            }

            booking.setBookingStatus(newBookingStatus);
            session.beginTransaction();
            session.merge(booking);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException("Error upon updating booking"
                    + " with id" + " " + id + " " + e.getMessage(), e);
        }
    }
    
    public List<TrainerBookingResponseDTO> getTrainerBookings(HttpHeaders headers){
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            
            User trainerProfile = (User) session.get(User.class, userId);
            if (trainerProfile == null) {
                throw new NoResultException("Trainer not found!");
            }
            
            List<Booking> bookings = session.createQuery(
                    "select b from Booking b "
                            + "left join fetch b.user u "
                            + "left join fetch b.trainingSession ts "
                            + "where ts.trainer.id = :trainerId", 
                    Booking.class)
                    .setParameter("trainerId", userId)
                    .list();
            
            return bookings.stream()
                    .map(this::convertToTrainerBookingResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching trainer booking"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }
}
