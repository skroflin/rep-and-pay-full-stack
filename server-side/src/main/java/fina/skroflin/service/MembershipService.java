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
import fina.skroflin.utils.jwt.JwtTokenUtil;
import fina.skroflin.utils.stripe.StripeConfig;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
@AllArgsConstructor
public class MembershipService extends MainService {

    private final JwtTokenUtil jwtTokenUtil;
    private final StripeConfig stripConfig;

    public MembershipService(JwtTokenUtil jwtTokenUtil, StripeConfig stripConfig) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.stripConfig = stripConfig;
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
                membership.getMembershipPrice()
        );
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
                o.membershipPrice()
        );

        session.beginTransaction();
        session.persist(m);
        session.getTransaction().commit();
    }

    public List<MembershipResponseDTO> getActiveMemberships() {
        try {
            List<Membership> memberships = session.createQuery(
                    "select m from Membership m"
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
                    + "left join fetch m.trainer "
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
                    + "left join fetch m.user "
                    + "where m.user.id = :userId "
                    + "order by m.startDate desc", Membership.class)
                    .setParameter("userId", userId)
                    .list();
            return memberships.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error upon fetching memberships"
                    + " " + "by id" + " " + userId
                    + " " + e.getMessage(), e);
        }
    }

    public boolean hasActiveMembership(HttpHeaders headers) {
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
                + "and m.endDate >= :today",
                Long.class)
                .setParameter("userId", userId)
                .setParameter("today", LocalDate.now())
                .uniqueResult();

        return count != null && count > 0;
    }

    public String createCheckoutSession(HttpHeaders headers, int price) {
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
                            .setSuccessUrl("http://localhost:8080/api/fina/skroflin/membership/success")
                            .setCancelUrl("http://localhost:8080/api/fina/skroflin/membership/cancel")
                            .setClientReferenceId(userId.toString())
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("eur")
                                                            .setUnitAmount((long) price)
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
            throw new RuntimeException(
                    "Error creating Stripe checkout session" + " "
                    + e.getMessage(),
                    e
            );
        }
    }
}
