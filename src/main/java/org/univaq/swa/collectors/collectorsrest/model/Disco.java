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
public class Disco {
    
    private Autore autore;
    private String titolo;
    private String anno;
    private List<Traccia> tracce;

    public Disco() {
    }

    public Disco(Autore autore, String titolo, String anno, List<Traccia> tracce) {
        this.autore = autore;
        this.titolo = titolo;
        this.anno = anno;
        this.tracce = tracce;
    }

    public Autore getAutore() {
        return autore;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAnno() {
        return anno;
    }

    public List<Traccia> getTracce() {
        return tracce;
    }
    
    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public void setTracce(List<Traccia> tracce) {
        this.tracce = tracce;
    }
    

}
