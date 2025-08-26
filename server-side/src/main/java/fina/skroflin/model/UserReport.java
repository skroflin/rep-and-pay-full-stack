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
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author skroflin
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "user_report")
@AttributeOverride(name = "id", column = @Column(name = "user_report_id"))
public class UserReport extends MainEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "membership_months")
    private List<LocalDate> membershipMonths;
}
