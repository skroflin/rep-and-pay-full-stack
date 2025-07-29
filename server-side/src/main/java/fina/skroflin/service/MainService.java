/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.service;

import fina.HibernateUtil;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.Session;

/**
 *
 * @author skroflin
 */
@MappedSuperclass
public abstract class MainService {
    protected Session session;

    public MainService() {
        session = HibernateUtil.getSession();
    }
    
}
