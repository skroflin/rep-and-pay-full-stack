/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author skroflin
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class Membership extends MainEntity{
    private Client client;
    private int month;
    private int year;
    private BigDecimal membershipPrice;
    private LocalDateTime paymentDate;
    private boolean isPaid;
}
