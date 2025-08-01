/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.Users;
import fina.skroflin.model.dto.booking.BookingDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.booking.user.MyBookingDTO;
import fina.skroflin.model.dto.booking.user.MyBookingResponseDTO;
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
        Integer userId = (booking.getId() != null)
                ? booking.getUser().getId() : null;
        Integer trainingSessionId = (booking.getId() != null)
                ? booking.getTrainingSession().getId() : null;

        return new BookingResponseDTO(
                booking.getId(),
                userId,
                trainingSessionId,
                booking.getReservationTime(),
                booking.getEndOfReservation()
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
                booking.getReservationTime(),
                booking.getEndOfReservation()
        );
    }

    @Transactional
    private Booking convertToEntity(BookingDTO dto) {
        Booking booking = new Booking();
        if (dto.userId() != null) {
            Users user = session.get(Users.class, dto.userId());
            if (user == null) {
                throw new IllegalArgumentException("User with the id"
                        + " " + dto.userId() + " " + "doesn't exist!");
            }
            booking.setUser(user);
        }
        if (dto.trainingSessionId() != null) {
            TrainingSession trainingSession = session.get(
                    TrainingSession.class, dto.trainingSessionId());
            if (trainingSession == null) {
                throw new IllegalArgumentException("Training session with the id"
                        + " " + dto.trainingSessionId() + " " + "doesn't exist!");
            }
            booking.setTrainingSession(trainingSession);
        }
        booking.setReservationTime(dto.reservationTime());
        booking.setEndOfReservation(dto.endOfReservationTime());
        return booking;
    }

    @Transactional
    private void updateEntityFromDto(Booking booking, BookingDTO dto) {
        if (dto.userId() != null) {
            Users user = session.get(Users.class, dto.userId());
            if (user == null) {
                throw new IllegalArgumentException("User with id"
                        + " " + dto.userId() + " " + "doesn't exist!");
            }
            booking.setUser(user);
        } else {
            booking.setUser(null);
        }
        if (dto.trainingSessionId() != null) {
            TrainingSession trainingSession = session.get(
                    TrainingSession.class, dto.trainingSessionId());
            if (trainingSession == null) {
                throw new IllegalArgumentException("Training session with id"
                        + " " + dto.trainingSessionId() + " " + "doesn't exist");
            }
            booking.setTrainingSession(trainingSession);
        } else {
            booking.setTrainingSession(null);
        }
        booking.setReservationTime(dto.reservationTime());
        booking.setEndOfReservation(dto.endOfReservationTime());
    }

    public List<BookingResponseDTO> getAll() {
        try {
            List<Booking> bookings = session.createQuery(
                    "select b from Booking b "
                    + "left join fetch b.user "
                    + "left join fetch b.trainingSession", Booking.class).list();
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
                    + "where b.userId = :userId",
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

    public BookingResponseDTO post(BookingDTO o) {
        try {
            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.user.id = :userId"
                    + "and :start < b.endOfReservationTime "
                    + "and :end > b.reservationTime", Long.class)
                    .setParameter("userId", o.userId())
                    .setParameter("start", o.reservationTime())
                    .setParameter("end", o.endOfReservationTime())
                    .uniqueResult();
            if (count > 0) {
                throw new IllegalArgumentException("You already have a booking"
                        + " " + "that overlaps with this time!");
            }

            Booking booking = convertToEntity(o);
            session.beginTransaction();
            session.persist(booking);
            session.getTransaction().commit();

            return convertToResponseDTO(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error upon creating booking:"
                    + e.getMessage(), e);
        }
    }

    public MyBookingResponseDTO createMyBooking(
            MyBookingDTO o,
            HttpHeaders headers
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            Users userProfile = (Users) session.get(Users.class, userId);
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
                    + "and :start < b.endOfReservationTime "
                    + "and :end > b.reservationTime", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("start", o.reservationTime())
                    .setParameter("end", o.endOfReservationTime())
                    .uniqueResult();
            if (count > 0) {
                throw new IllegalArgumentException("You already have a booking"
                        + " " + "that overlaps with this time!");
            }

            Booking booking = new Booking(
                    userProfile,
                    ts,
                    o.reservationTime(),
                    o.endOfReservationTime()
            );

            session.beginTransaction();
            session.persist(booking);
            session.getTransaction().commit();

            return convertToMyResponseDTO(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error upon creating booking:"
                    + e.getMessage(), e);
        }
    }

    public BookingResponseDTO put(BookingDTO o, int id) {
        try {
            Booking existingBooking
                    = (Booking) session.get(Booking.class, id);
            if (existingBooking == null) {
                throw new NoResultException("Booking with id" + " "
                        + id + " " + "doesn't exist!");
            }

            Long count = session.createQuery(
                    "select count(b) from Booking b "
                    + "where b.userId = :userId "
                    + "and b.id != :currentId "
                    + "and :start < b.endOfReservationTime "
                    + "and :end > b.reservationTime", Long.class)
                    .setParameter("userId", o.userId())
                    .setParameter("start", o.reservationTime())
                    .setParameter("end", o.endOfReservationTime())
                    .setParameter("currentId", id)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("You already have a booking"
                        + " " + "that overlaps with this time!");
            }

            Users user
                    = (Users) session.get(Users.class, o.userId());
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

            updateEntityFromDto(existingBooking, o);
            session.beginTransaction();
            session.merge(existingBooking);
            session.getTransaction().commit();
            return convertToResponseDTO(existingBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error upon updating booking with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public MyBookingResponseDTO updateMyBooking(
            MyBookingDTO o,
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
                    + "where b.userId = :userId "
                    + "and b.id != :currentId "
                    + "and :start < b.endOfReservationTime "
                    + "and :end > b.reservationTime", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("start", o.reservationTime())
                    .setParameter("end", o.endOfReservationTime())
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
            existingBooking.setReservationTime(o.reservationTime());
            existingBooking.setEndOfReservation(o.endOfReservationTime());

            session.beginTransaction();
            session.merge(existingBooking);
            session.getTransaction().commit();

            return convertToMyResponseDTO(existingBooking);
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
}
