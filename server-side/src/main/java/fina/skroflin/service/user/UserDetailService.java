/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service.user;

import fina.skroflin.model.Users;
import fina.skroflin.service.MainService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import org.hibernate.Session;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author skroflin
 */
@Service
public class UserDetailService extends MainService implements UserDetailsService{

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        Users user = null;
        try(Session sess = session.getSessionFactory().openSession()) {
            user = sess.createQuery("from Users u "
                            + "where u.username = :username", Users.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException(
                    "User with the given username" 
                            + " " + username + " "
                            + "doesn't exist!"
            );
        }
        
        GrantedAuthority grantedAuthority = 
                new SimpleGrantedAuthority("ROLE_" + user.getRole());
        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(grantedAuthority)
        );
    }    
}
