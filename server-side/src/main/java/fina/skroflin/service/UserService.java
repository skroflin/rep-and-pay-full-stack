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
import fina.skroflin.model.dto.user.password.PasswordResponseDTO;
import fina.skroflin.model.enums.Role;
import fina.skroflin.service.BookingService;
import fina.skroflin.service.MainService;
import fina.skroflin.service.TrainingSessionService;
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
            bookings = user.getBookings() == null
                    ? Collections.emptyList()
                    : user.getBookings().stream()
                            .map(bookingService::convertToResponseDTO)
                            .collect(Collectors.toList());
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
                user.getMembershipMonth(),
                trainingSessions,
                bookings
        );
    }

    @Transactional
    private PasswordResponseDTO convertPassToResponseDTO(
            User user
    ) {
        return new PasswordResponseDTO(
                user.getId(),
                user.getUsername()
        );
    }

    @Transactional
    private User convertToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        return user;
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

    @Transactional
    private void updatePassEntityFromDto(User user, PasswordDTO dto) {
        user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
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

    public UserResponseDTO updateMyProfile(HttpHeaders headers, UserRequestDTO o) {
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

            updateEntityFromDto(userProfile, o);

            session.beginTransaction();
            session.merge(userProfile);
            session.getTransaction().commit();

            return convertToResponseDTO(userProfile);
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

    public UserResponseDTO registration(RegistrationDTO o) {
        try {
            session.beginTransaction();

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
            session.persist(newUser);
            session.getTransaction().commit();
            return convertToResponseDTO(newUser);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error upon registration"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public UserResponseDTO login(LoginDTO o) {
        try {
            User user = session.createQuery("from User u where u.username = :username",
                    User.class)
                    .setParameter("username", o.username())
                    .getSingleResult();
            if (!bCryptPasswordEncoder.matches(o.password(), user.getPassword())) {
                throw new IllegalArgumentException("Wrong password");
            }
            return convertToResponseDTO(user);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error upon logging in"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public PasswordResponseDTO changeMyPassword(PasswordDTO o, HttpHeaders headers) {
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
            updatePassEntityFromDto(userPassword, o);
            session.beginTransaction();
            session.getTransaction().commit();
            return convertPassToResponseDTO(userPassword);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error changing password"
                    + " " + e.getMessage(),
                    e
            );
        }
    }
    /*    
    public User getUserByEmail(String email){
        try {
            return session.createQuery(
                    "from User u where u.email = :email",
                    User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("User with email" + " " 
                    + email + " " + "doesn't exist!");
        }
    }
    
    public User getUserByUsername(String username){
        try {
            return session.createQuery(
                    "from User u where u.username = :username",
                    User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("User with username" + " "
                    + username + " " + "doesn't exist!");
        }
    }
     */
}
