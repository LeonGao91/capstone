/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intel;

/**
 *
 * @author Junying
 */
public class Users {
    private String username; //primary key
    private String password;
    private String folders; //a string that use "," as delimiter to seperate the folders name

    public Users() {
    }

    public Users(String username, String password, String folders) {
        this.username = username;
        this.password = password;
        this.folders = folders;
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

    public String getFolders() {
        return folders;
    }

    public void setFolders(String folders) {
        this.folders = folders;
    }
    
    
}
