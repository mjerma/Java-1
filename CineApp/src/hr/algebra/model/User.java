/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author Mihael
 */
public class User {
    
    private final String username;
    private final String password;
    private final boolean isAdministrator;

    public User(String username, String password, boolean isAdministrator) {
        this.username = username;
        this.password = password;
        this.isAdministrator = isAdministrator;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }
}
