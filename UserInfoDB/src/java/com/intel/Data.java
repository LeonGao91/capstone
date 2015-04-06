/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

/**
 *
 * @author Junying
 */
@ManagedBean
@SessionScoped
public class Data {
    
    private Users u;
    private HibernateUtil helper;
    private Session session;  
    private String password;
    private String folders;
    
    /**
     * This method search the database return the hash password given the username
     * @param username
     * @return hash password
     */
    public String getPassword(String username){
        session=helper.getSessionFactory().openSession();
        session.beginTransaction();
        u = (Users)session.get(Users.class, username);
        this.password = u.getPassword();
        return password;
    }
    
    /**
     * This method validate the user information and match the information with the data stored in the database
     * @param username the username got from the interface
     * @param pw the hash password 
     * @return a string to indicate different situations
     */
    public String validateUser(String username,String pw){
        
        if (username==null){
            return "Username should not be null"; 
        }
        // if this username does not exist in database, return null
        try{
            password = getPassword(username);
        }
        catch(NullPointerException ex){
            return null;
        }
        if (pw==password){
            return "Match"; //when password match the information stored in database
        }
        return "Unmatch";
    }
    
    /**
     * This method returns a string of folder names which uses "," as delimiter
     * @param username
     * @return a string of folder names
     */
    public String getFolders(String username) {
        session=helper.getSessionFactory().openSession();
        session.beginTransaction();
        u = (Users)session.get(Users.class, username);
        this.folders = u.getFolders();
        return folders;
    }

}

