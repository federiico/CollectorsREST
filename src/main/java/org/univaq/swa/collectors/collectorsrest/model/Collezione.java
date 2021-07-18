/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author federicocantoro
 */
public class Collezione {
    
    private String titolo;
    private String privacy;
    private List<Utente> utenti;
    private List<Map<String,Object>> dischi;

    public Collezione() {
    }

    public Collezione(String titolo, String privacy, List<Utente> utenti, List<Map<String,Object>> dischi) {
        this.titolo = titolo;
        this.privacy = privacy;
        this.utenti = utenti;
        this.dischi = dischi;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getPrivacy() {
        return privacy;
    }

    public List<Utente> getUtenti() {
        return utenti;
    }

    public List<Map<String,Object>> getDischi() {
        return dischi;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
    }

    public void setDischi(List<Map<String,Object>> dischi) {
        this.dischi = dischi;
    }
    
}
