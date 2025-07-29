/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author skroflin
 */
@Entity
public class Membership extends MainEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "membership_id")
    private Integer id;
    private Client client;
    private int month;
    private int year;
    @Column(name = "membership_price", columnDefinition = "float")
    private BigDecimal membershipPrice;
    @Column(name = "payment_date", columnDefinition = "datetime")
    private Date paymentDate;
    @Column(name = "is_paid", columnDefinition = "bit")
    private boolean isPaid;
}
