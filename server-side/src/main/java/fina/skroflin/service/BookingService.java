/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.Booking;
import fina.skroflin.model.TrainingSession;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.booking.BookingDTO;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author skroflin
 */
public class BookingService extends MainService {

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
    private Booking convertToEntity(BookingDTO dto) {
        Booking booking = new Booking();
        if (dto.userId() != null) {
            User user = session.get(User.class, dto.userId());
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
                        + " " + dto.userId() + " " + "doesn't exist!");
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
            User user = session.get(User.class, dto.userId());
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
            
            User user = 
                    (User) session.get(User.class, o.userId());
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
    
    public String delete(int id){
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
}
