/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.model;

import fina.skroflin.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 *
 * @author skroflin
 */
@Entity
@Table(name = "users")
public class User extends MainEntity {

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private Role role;
    @Column(name = "is_membership_paid")
    private boolean isMembershipPaid;
    @Column(name = "membership_month")
    private Date membershipMonth;
    @OneToMany(mappedBy = "trainer")
    private List<TrainingSession> trainingSessions;
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public User() {
    }

    public User(
            String firstName,
            String lastName,
            String email, 
            String username, 
            String password, 
            Role role, 
            boolean isMembershipPaid, 
            Date memberShipMonth, 
            List<TrainingSession> trainingSessions, 
            List<Booking> bookings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isMembershipPaid = isMembershipPaid;
        this.membershipMonth = membershipMonth;
        this.trainingSessions = trainingSessions;
        this.bookings = bookings;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isIsMembershipPaid() {
        return isMembershipPaid;
    }

    public void setIsMembershipPaid(boolean isMembershipPaid) {
        this.isMembershipPaid = isMembershipPaid;
    }

    public Date getMembershipMonth() {
        return membershipMonth;
    }

    public void setMembershipMonth(Date memberShipMonth) {
        this.membershipMonth = memberShipMonth;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}
