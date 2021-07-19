/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.model;

/**
 *
 * @author federicocantoro
 */
public class Autore {
    
    private String nome;
    private String cognome;
    private String nome_arte;

    public Autore() {
    }
    
     public Autore( String nome_arte) {
        this.nome_arte = nome_arte;
    }
    
    public Autore(String nome, String cognome, String nome_arte) {
        this.nome = nome;
        this.cognome = cognome;
        this.nome_arte = nome_arte;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome_arte() {
        return nome_arte;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome_arte(String nome_arte) {
        this.nome_arte = nome_arte;
    }
    
    
    
    
    
}
