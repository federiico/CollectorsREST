/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;

/**
 *
 * @author federicocantoro
 */
public class CollezioneResources {
    
    private final Collezione f;

    CollezioneResources(Collezione f) {
        this.f=f;
    }
    
        @GET
    @Produces("application/json")
    public Response getCollezione() {
        try {
            return Response.ok(f)
                    //possiamo aggiungere alla Response vari elementi, ad esempio header...
                    .header("X-fattura-app-version", "1.0")
                    .build();
        } catch (Exception e) {
            //gestione delle eccezioni (business):
            //Modalità 1: creazione response di errore
//            return Response.serverError()
//                    .entity(e.getMessage()) //mai in produzione
//                    .build();
            //Modalità 2: incapsulamento in eccezione JAXRS compatibile
            throw new RESTWebApplicationException(e);
        }
    }
    
}
