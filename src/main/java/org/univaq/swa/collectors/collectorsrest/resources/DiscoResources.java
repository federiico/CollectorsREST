/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.univaq.swa.collectors.collectorsrest.security.Logged;
import javax.ws.rs.WebApplicationException;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.ws.rs.FormParam;
import org.univaq.swa.collectors.collectorsrest.model.Autore;
import org.univaq.swa.collectors.collectorsrest.model.Traccia;
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

    @Logged
    @GET
    @Produces("application/json")
    public Response getDisco() {
        try {
            return Response.ok(disco).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
}
