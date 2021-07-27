/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;
import javax.sql.DataSource;
import org.univaq.swa.collectors.collectorsrest.model.Disco;

/**
 *
 * @author federicocantoro
 */
public class DiscoResources {

    DataSource dataSource = null;

    private final Disco disco;

    DiscoResources(Disco disco) {
        this.disco = disco;
    }
    
    
    @Produces("application/json")
    public Object getDisco() {
        try {
            return Response.ok(disco).build().getEntity();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
}
