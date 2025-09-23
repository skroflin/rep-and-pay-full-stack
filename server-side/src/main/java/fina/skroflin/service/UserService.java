/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.User;
import fina.skroflin.model.dto.booking.BookingResponseDTO;
import fina.skroflin.model.dto.training.TrainingSessionResponseDTO;
import fina.skroflin.model.dto.user.LoginDTO;
import fina.skroflin.model.dto.user.RegistrationDTO;
import fina.skroflin.model.dto.user.UserRequestDTO;
import fina.skroflin.model.dto.user.UserResponseDTO;
import fina.skroflin.model.dto.user.password.PasswordDTO;
import fina.skroflin.model.enums.Role;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class UserService extends MainService {

    private final TrainingSessionService trainingSessionService;
    private final BookingService bookingService;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            TrainingSessionService trainingSessionService,
            BookingService bookingService,
            JwtTokenUtil jwtTokenUtil,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.trainingSessionService = trainingSessionService;
        this.bookingService = bookingService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    private UserResponseDTO convertToResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        List<TrainingSessionResponseDTO> trainingSessions
                = Collections.emptyList();
        if (user.getRole() == Role.coach) {
            trainingSessions = user.getTrainingSessions() == null
                    ? Collections.emptyList()
                    : user.getTrainingSessions().stream()
                            .map(trainingSessionService::convertToResponseDTO)
                            .collect(Collectors.toList());

        }

        List<BookingResponseDTO> bookings
                = Collections.emptyList();
        if (user.getRole() == Role.user) {
            if (user.getBookings() != null) {
                bookings = user.getBookings().stream()
                        .map(bookingService::convertToResponseDTO)
                        .collect(Collectors.toList());
            }
        }

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.isIsMembershipPaid(),
                trainingSessions,
                bookings
        );
    }

    @Transactional
    private void updateEntityFromDto(User user, UserRequestDTO dto) {
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setRole(dto.role());
    }

    public List<UserResponseDTO> getAll() {
        List<User> users = session.createQuery("from User u", User.class).list();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllCoaches() {
        List<User> users = session.createQuery(
                "from User u where u.role = coach",
                User.class).list();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = session.createQuery(
                "from User u where u.role = user",
                User.class).list();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getUserByName(String username) {
        try {
            List<User> users = session.createQuery(
                    "from User u where u.role = user and lower(u.username) like lower (:username)", User.class)
                    .setParameter("username", "%" + username + "%")
                    .list();
            return users.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error there is no user with the username"
                    + " " + username + " " + e.getMessage(),
                    e
            );
        }
    }

    public UserResponseDTO getMyProfile(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            User users = (User) session.get(User.class, userId);
            if (users == null) {
                throw new NoResultException(
                        "User with id"
                        + " " + userId + " "
                        + "doesn't exist!"
                );
            }
            return convertToResponseDTO(users);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error retrieving user from token"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public void updateMyProfile(HttpHeaders headers, UserRequestDTO o) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            User userProfile = (User) session.get(User.class, userId);
            if (userProfile == null) {
                throw new NoResultException(
                        "User with id"
                        + " " + userId + " "
                        + "doesn't exist!"
                );
            }

            Long count = session.createQuery(
                    "select count(u) from User u "
                    + "where (u.username = :username "
                    + "or u.email = :email) "
                    + "and u.id = :currentId", Long.class)
                    .setParameter("username", o.username())
                    .setParameter("email", o.email())
                    .setParameter("currentId", userId)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("There is already a user"
                        + " " + "with the same username or email!");
            }

            userProfile.setFirstName(o.firstName());
            userProfile.setLastName(o.lastName());
            userProfile.setEmail(o.email());
            userProfile.setUsername(o.username());
            userProfile.setPassword(bCryptPasswordEncoder.encode(o.password()));
            userProfile.setRole(o.role());
            userProfile.setIsMembershipPaid(o.isMembershipPaid());

            session.beginTransaction();
            session.merge(userProfile);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error upon updating user"
                    + "profile:" + " " + e.getMessage(), e);
        }
    }

    public String deleteMyProfile(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            User userProfile = (User) session.get(User.class, userId);
            if (userProfile == null) {
                throw new NoResultException(
                        "User with id"
                        + " " + userId + " "
                        + "doesn't exist!"
                );
            }

            session.beginTransaction();
            session.remove(userProfile);
            session.getTransaction().commit();
            return "User with" + " " + userId + " " + "has been deleted!";
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error deleting user profile"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public UserResponseDTO getById(int id) {
        User user = session.get(User.class, id);
        return convertToResponseDTO(user);
    }

    public UserResponseDTO put(UserRequestDTO o, int id) {
        try {
            User existingUser = (User) session.get(User.class, id);
            if (existingUser == null) {
                throw new NoResultException("User with id" + " "
                        + id + " " + "doesn't exist!");
            }
            Long count = session.createQuery(
                    "select count(u) from User u "
                    + "where (u.username = :username "
                    + "or u.email = :email) "
                    + "and u.id = :currentId", Long.class)
                    .setParameter("username", o.username())
                    .setParameter("email", o.email())
                    .setParameter("currentId", id)
                    .uniqueResult();

            if (count > 0) {
                throw new IllegalArgumentException("There is already a user"
                        + " " + "with the same username or email!");
            }

            existingUser.setFirstName(o.firstName());
            existingUser.setLastName(o.lastName());
            existingUser.setEmail(o.email());
            existingUser.setUsername(o.username());
            existingUser.setPassword(bCryptPasswordEncoder.encode(o.password()));
            existingUser.setRole(o.role());
            existingUser.setIsMembershipPaid(o.isMembershipPaid());

            updateEntityFromDto(existingUser, o);
            session.beginTransaction();
            session.merge(existingUser);
            session.getTransaction().commit();
            return convertToResponseDTO(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error upon updating user with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public String delete(int id) {
        try {
            User user = (User) session.get(User.class, id);
            if (user == null) {
                throw new NoResultException("User with id"
                        + " " + id + " " + "doesn't exist!");
            }
            return "User with id" + " " + id + " " + "deleted!";
        } catch (Exception e) {
            throw new RuntimeException("Error upon deleting user with id"
                    + " " + id + " " + e.getMessage(), e);
        }
    }

    public void registration(RegistrationDTO o) {
        try {

            Long count = session.createQuery(
                    "select count(u) from User u "
                    + "where (u.username = :username "
                    + "or u.email = :email) ", Long.class)
                    .setParameter("username", o.username())
                    .setParameter("email", o.email())
                    .uniqueResult();
            if (count > 0) {
                throw new IllegalArgumentException("There is already a user"
                        + " " + "with the same username or email!");
            }

            User newUser = new User(
                    o.firstName(),
                    o.lastName(),
                    o.email(),
                    o.username(),
                    bCryptPasswordEncoder.encode(o.password()),
                    o.role()
            );

            session.beginTransaction();
            session.persist(newUser);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error upon registration"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public void login(LoginDTO o) {
        try {
            User user = session.createQuery("from User u where u.username = :username",
                    User.class)
                    .setParameter("username", o.username())
                    .getSingleResult();
            if (!bCryptPasswordEncoder.matches(o.password(), user.getPassword())) {
                throw new IllegalArgumentException("Wrong password");
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error upon logging in"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public void changeMyPassword(PasswordDTO o, HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));
            User userPassword = (User) session.get(User.class, userId);
            if (userPassword == null) {
                throw new NoResultException(
                        "User with id"
                        + " " + userId + " "
                        + "doesn't exist!"
                );
            }
            userPassword.setPassword(bCryptPasswordEncoder.encode(o.password()));
            session.beginTransaction();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error changing password"
                    + " " + e.getMessage(),
                    e
            );
        }
    }
}
