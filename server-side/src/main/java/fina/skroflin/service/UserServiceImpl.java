/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.skroflin.model.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class UserServiceImpl extends MainService {
    public User getUserByEmail(String email) {
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
}
