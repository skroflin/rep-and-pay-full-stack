package ffos;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Katedra
 */
// https://www.geeksforgeeks.org/singleton-class-java/

// ostalo čitati na https://sourcemaking.com/design_patterns
//Singleton
public class HibernateUtil {
    
    private static Session session = null;
    
    private HibernateUtil(){
        try {
             session = new Configuration().configure().buildSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
    public static Session getSession(){
        if(session==null){
           new HibernateUtil();
        }
        return session;
    }
    
    
}