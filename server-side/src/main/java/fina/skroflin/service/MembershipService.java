/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import fina.skroflin.model.Membership;
import fina.skroflin.model.User;
import fina.skroflin.model.dto.membership.MembershipRequestDTO;
import fina.skroflin.model.dto.membership.MembershipResponseDTO;
import fina.skroflin.model.dto.membership.user.MonthOptionResponseDTO;
import fina.skroflin.model.dto.membership.user.MyMembershipResponseDTO;
import fina.skroflin.model.dto.stripe.CheckoutSessionRequestDTO;
import fina.skroflin.model.dto.stripe.StripeCheckoutResponseDTO;
import fina.skroflin.utils.jwt.JwtTokenUtil;
import fina.skroflin.utils.stripe.StripeConfig;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class MembershipService extends MainService {

    private final JwtTokenUtil jwtTokenUtil;
    private final StripeConfig stripeConfig;

    public MembershipService(JwtTokenUtil jwtTokenUtil, StripeConfig stripeConfig) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.stripeConfig = stripeConfig;
    }

    @Transactional
    public MembershipResponseDTO convertToResponseDTO(Membership membership) {
        if (membership == null) {
            return null;
        }

        return new MembershipResponseDTO(
                membership.getId(),
                membership.getUser().getFirstName(),
                membership.getUser().getLastName(),
                membership.getStartDate(),
                membership.getEndDate(),
                membership.getMembershipPrice(),
                membership.getPaymentDate(),
                membership.isAlreadyPaid()
        );
    }

    @Transactional
    public MyMembershipResponseDTO convertToMyResponse(Membership membership) {
        if (membership == null) {
            return null;
        }

        return new MyMembershipResponseDTO(
                membership.getId(),
                membership.getStartDate(),
                membership.getEndDate(),
                membership.getMembershipPrice(),
                membership.getPaymentDate(),
                membership.isAlreadyPaid()
        );
    }

    @Transactional
    public MonthOptionResponseDTO convertToMonthOptionResponse(Membership membership, YearMonth month) {
        boolean paid = YearMonth.from(membership.getStartDate()).equals(month);
        BigDecimal price = BigDecimal.valueOf(membership.getMembershipPrice() / 100.0);
        String label = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + month.getYear();

        return new MonthOptionResponseDTO(label, price, paid, true);
    }

    public List<MyMembershipResponseDTO> getMyMemberships(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            User user = (User) session.get(User.class, userId);
            if (user == null) {
                throw new NoResultException("User not found!");
            }
            List<Membership> memberships = session.createQuery(
                    "from Membership m where m.user.id = :userId", Membership.class)
                    .setParameter("userId", userId)
                    .list();
            return memberships.stream()
                    .map(this::convertToMyResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public void post(MembershipRequestDTO o) {
        User user = (User) session.get(User.class, o.userId());
        if (user == null || !user.equals(user.getRole())) {
            throw new IllegalArgumentException(
                    "User with the id" + " "
                    + o.userId() + " "
                    + "doesn't exist or isn't a user!"
            );
        }

        Long count = session.createQuery(
                "select count(m) from Membership m "
                + "where m.user.id = :userId "
                + "and m.startDate = :newDate "
                + "and m.endDate = :newDate",
                Long.class)
                .setParameter("userId", o.userId())
                .setParameter("newDate", o.startDate())
                .setParameter("newDate", o.endDate())
                .uniqueResult();

        if (count > 0) {
            throw new IllegalArgumentException("");
        }

        Membership m = new Membership(
                user,
                o.startDate(),
                o.endDate(),
                o.membershipPrice(),
                o.alreadyPaid()
        );

        session.beginTransaction();
        session.persist(m);
        session.getTransaction().commit();
    }

    public List<MembershipResponseDTO> getActiveMemberships() {
        try {
            List<Membership> memberships = session.createQuery(
                    "select m from Membership m "
                    + "left join fetch m.user "
                    + "where m.startDate <= :today "
                    + "and m.endDate >= :today", Membership.class)
                    .setParameter("today", LocalDate.now())
                    .list();
            return memberships.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public List<MembershipResponseDTO> getExpiredMemberships() {
        try {
            List<Membership> memberships = session.createQuery(
                    "select m from Membership m "
                    + "left join fetch m.user "
                    + "where m.endDate < :today", Membership.class)
                    .setParameter("today", LocalDate.now())
                    .list();
            return memberships.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public List<MembershipResponseDTO> getMembershipByUser(int userId) {
        try {

            List<Membership> memberships = session.createQuery(
                    "select m from Membership m "
                    + "left join fetch m.user u "
                    + "where u.id = :userId "
                    + "order by m.startDate desc", Membership.class)
                    .setParameter("userId", userId)
                    .list();

            if (memberships.isEmpty()) {
                throw new IllegalArgumentException(
                        "User with id"
                        + " " + userId
                        + " " + "has no memberships!"
                );
            }
            return memberships.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error upon fetching memberships"
                    + " " + e.getMessage(),
                    e
            );
        }
    }

    public boolean hasActiveMembership(HttpHeaders headers, LocalDate selectedDate) {
        String token = jwtTokenUtil.extractTokenFromHeaders(headers);
        Integer userId = jwtTokenUtil.extractClaim(token,
                claims -> claims.get("UserId", Integer.class));

        User user = (User) session.get(User.class, userId);
        if (user == null) {
            throw new NoResultException("User not found!");
        }

        Long count = session.createQuery(
                "select count(m) from Membership m "
                + "where m.user.id = :userId "
                + "and m.startDate <= :today "
                + "and m.endDate >= :today ",
                Long.class)
                .setParameter("userId", userId)
                .setParameter("today", selectedDate)
                .uniqueResult();
        
        System.out.println("Aktivna članarina" + " " + count);

        return count != null && count > 0;
    }

    public String createCheckoutSession(HttpHeaders headers, CheckoutSessionRequestDTO o) {
        String token = jwtTokenUtil.extractTokenFromHeaders(headers);
        Integer userId = jwtTokenUtil.extractClaim(token,
                claims -> claims.get("UserId", Integer.class));

        User user = (User) session.get(User.class, userId);
        if (user == null) {
            throw new NoResultException("User not found!");
        }

        try {
            SessionCreateParams params
                    = SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:5173/?status=success")
                            .setCancelUrl("http://localhost:5173/?status=cancel")
                            .setClientReferenceId(userId.toString())
                            .putMetadata("month", String.valueOf(o.month()))
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("eur")
                                                            .setUnitAmount((long) o.price())
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Gym membership")
                                                                            .setDescription("Gym membership for Rep & Pay")
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();
            Session sessionStripe = Session.create(params);
            return sessionStripe.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Error upon creating checkout session!"
                    + " " + "by id" + " " + userId
                    + " " + e.getMessage(), e);
        }
    }

    public void activateMembership(Integer userId, int price, int month) {

        User user = (User) session.get(User.class, userId);
        if (user == null) {
            throw new NoResultException("User not found!");
        }

        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Long count = session.createQuery(
                "select count(m) from Membership m "
                + "where m.user.id = :userId "
                + "and m.startDate <= :endDate "
                + "and m.endDate >= :startDate",
                Long.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .uniqueResult();

        if (count > 0) {
            throw new IllegalArgumentException("There is already a membership "
                    + "for this user" + " " + userId + " " + "and the given month"
                    + " " + month + "/" + year);
        }

        Membership newMembership = new Membership(user, startDate, endDate, price, true);

        session.beginTransaction();
        session.persist(newMembership);
        session.getTransaction().commit();
    }

    public Long getNumOfMemberships() {
        try {
            Long numOfMemberships = session.createQuery(
                    "select count(m.id) from Membership m", Long.class)
                    .getSingleResult();
            return numOfMemberships;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching number of memberships"
                    + " " + "sessions" + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfActiveMemberships() {
        try {
            Long numOfActiveMemberships = session.createQuery(
                    "select count(m.id) from Membership m "
                    + "where m.startDate <= :today "
                    + "and m.endDate >= :today", Long.class)
                    .setParameter("today", LocalDate.now())
                    .uniqueResult();
            return numOfActiveMemberships;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public Long getNumOfExpiredMemberships() {
        try {
            Long numOfExpiredMemberships = session.createQuery(
                    "select count(m.id) from Membership m "
                    + "where m.endDate < :today", Long.class)
                    .setParameter("today", LocalDate.now())
                    .uniqueResult();
            return numOfExpiredMemberships;
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public List<MembershipResponseDTO> getAllMemberships() {
        try {
            List<Membership> memberships = session.createQuery(
                    "select m from Membership m left join fetch m.user",
                    Membership.class)
                    .list();
            return memberships.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships:"
                    + " " + e.getMessage(), e);
        }
    }

    public List<MonthOptionResponseDTO> getMonthOptions(HttpHeaders headers) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeaders(headers);
            Integer userId = jwtTokenUtil.extractClaim(token,
                    claims -> claims.get("UserId", Integer.class));

            User user = (User) session.get(User.class, userId);
            if (user == null) {
                throw new NoResultException("User not found!");
            }
            List<Membership> memberships = session.createQuery(
                    "select m from Membership m where m.user.id = :userId",
                    Membership.class)
                    .setParameter("userId", userId)
                    .list();

            LocalDate now = LocalDate.now();
            YearMonth currentMonth = YearMonth.from(now);
            YearMonth nextMonth = currentMonth.plusMonths(1);

            List<YearMonth> allowedMonths = List.of(currentMonth, nextMonth);

            return allowedMonths.stream()
                    .map(month -> {
                        Membership matchingMembership = memberships.stream()
                                .filter(m -> YearMonth.from(m.getStartDate()).equals(m))
                                .findFirst()
                                .orElse(null);

                        if (matchingMembership != null) {
                            return convertToMonthOptionResponse(matchingMembership, month);
                        } else {
                            String label = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + month.getYear();
                            return  new MonthOptionResponseDTO(label, BigDecimal.valueOf(30.00), false, true);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching month options:"
                    + " " + e.getMessage(), e);
        }
    }
}
