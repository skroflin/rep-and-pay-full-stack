/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author skroflin
 */
@Entity
@Table(name = "membership")
@AttributeOverride(name = "id", column = @Column(name = "membership_id"))
public class Membership extends MainEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "membership_price", columnDefinition = "int")
    private long membershipPrice;

    public Membership() {
    }

    public Membership(User user, LocalDate startDate, LocalDate endDate, long membershipPrice) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.membershipPrice = membershipPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getMembershipPrice() {
        return membershipPrice;
    }

    public void setMembershipPrice(long membershipPrice) {
        this.membershipPrice = membershipPrice;
    }
    
    
}
