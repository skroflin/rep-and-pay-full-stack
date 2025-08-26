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
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author skroflin
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "membership")
@AttributeOverride(name = "id", column = @Column(name = "membership_id"))
public class Membership extends MainEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "membership_price", columnDefinition = "float")
    private BigInteger membershipPrice;
}
