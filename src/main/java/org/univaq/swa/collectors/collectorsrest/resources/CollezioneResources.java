/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;
import org.univaq.swa.collectors.collectorsrest.security.Logged;

/**
 *
 * @author federicocantoro
 */

public class CollezioneResources {
    
    private final Collezione collezione;

    CollezioneResources(Collezione collezione) {
        this.collezione=collezione;
    }
    
    @Logged
    @GET
    @Produces("application/json")
    public Response getCollezione() {
        try {
            return Response.ok(collezione).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
}
