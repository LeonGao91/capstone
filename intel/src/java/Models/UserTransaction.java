/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Models;
import java.util.ArrayList;
import org.hibernate.Session;

/**
 *
 * @author leon
 */
public class UserTransaction {
    
    public boolean validateUser(String id, String password) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        User user = (User) session.get(User.class, id);
        session.getTransaction().commit();
        return user != null && ((String)user.getPassword()).equals(password);
        
    }
    
    public String getFolder(String id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        User user = (User) session.get(User.class, id);
        session.getTransaction().commit();
        return user.getFolders();
    }
}
