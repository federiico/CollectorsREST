/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
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
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.model.Utente;

/**
 *
 * @author federicocantoro
 */
@Logged
@Path("utente")
public class UtenteResources {

    DataSource dataSource = null;

    @GET
    @Path("collezioni")
    @Produces("application/json")
    public Response getCollezioni(
            @QueryParam ("privacy") String privacy,
            @QueryParam ("utente") String utente,
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ){
        if(privacy.equalsIgnoreCase("personale"))
            return getCollezioniPersonali(uriinfo, req);
        else
            return getCollezioniCondivise(utente,uriinfo, req);
    }
    
    
    @Produces("application/json")
    public Response getCollezioniPersonali(
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {

        String username = req.getProperty("username").toString();

        //System.out.println("FUNZIONA:" + username);
        //return null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            String sql = "select titolo "
                    + "from collezione, utente "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale'";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> collezioni = new ArrayList<String>();

            while (rs.next()) {
                URI uri = uriinfo.getBaseUriBuilder().path(CollezioniResources.class)
                        .path(CollezioniResources.class, "getCollezione").build(rs.getString("titolo"));
                collezioni.add(uri.toString());
            }
            return Response.ok(collezioni).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
    

    @Produces("application/json")
    public Response getCollezioniCondivise(
            @QueryParam("utente") String utente,
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {
        
        String username = req.getProperty("username").toString();

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            String sql = "SELECT * from collezione, utente, collezione_condivisa as coll_cond where utente.username = ? "+       
                         " and coll_cond.id_collezione = collezione.id;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> collezioni = new ArrayList<String>();

            while (rs.next()) {
                URI uri = uriinfo.getBaseUriBuilder().path(CollezioniResources.class)
                        .path(CollezioniResources.class, "getCollezione").build(rs.getString("titolo"));
                collezioni.add(uri.toString());
            }
            return Response.ok(collezioni).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
        
    }
    
}
