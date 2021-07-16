/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.model;

import java.util.List;
/**
 *
 * @author federicocantoro
 */
public class Utente {
    
    private String username;
    private String password;
    private List<Collezione> collezioni;
    private String token;

    public Utente() {
    }
    
    public Utente(String username, String password, List<Collezione> collezioni, String token) {
        this.username = username;
        this.password = password;
        this.collezioni = collezioni;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Collezione> getCollezioni() {
        return collezioni;
    }
    
    
    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCollezioni(List<Collezione> collezioni) {
        this.collezioni = collezioni;
    }
    
     public void setToken(String token) {
        this.token = token;
    }
    
}
