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
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.model.Utente;

/**
 *
 * @author federicocantoro
 */

@Path("utente")
public class UtenteResources {

    DataSource dataSource = null;
    
    @GET
    @Logged
    @Path("collezioni")
    @Produces("application/json")
    public Response getCollezioniPersonali(
           
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {
      // System.out.println("USERNAME " + req.getProperty("username") );
       
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
                URI uri = uriinfo.getBaseUriBuilder()
                    .path(getClass()).path("collezioni")
                    .path(getClass(), "getCollezione").build(rs.getString("titolo"));
                collezioni.add(uri.toString());
            }
            return Response.ok(collezioni).build();
            
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }
    
    @Path("collezioni/{titolo: [a-zA-Z0-9_]*}")
    public CollezioneResources getCollezione(
            @PathParam("titolo") String titolo
         
    ) {
        Collezione c = new Collezione();
        Disco d = new Disco();
        d.setTitolo("S.A.L.M.O");
        Autore a = new Autore("fabrizio","pisciottu","Salmo");
        d.setAutore(a);
        c.setTitolo("PLAYLIST");
        c.setPrivacy("personale");
        List<Disco> dischi = new ArrayList<Disco>();
        dischi.add(d);
        c.setDischi(dischi);
        Utente u = new Utente();
        u.setUsername("mario");
        u.setPassword("mario123");
        return new CollezioneResources(c);
        
    }

}
