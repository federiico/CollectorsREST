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
@Path("dischi")
public class DischiResources {
    
    DataSource dataSource = null;
    
    @Path("{disco: [a-zA-Z0-9]+}")
    @Produces("application/json")
    public DiscoResources getDisco(
            @PathParam("collezione") String collezione,
            @PathParam("disco") String disco,
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req){
        
        try{
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        }
        catch(Exception e){throw new RESTWebApplicationException(e);}
            
        Connection connection = null;
        PreparedStatement preparedStatement = null;
            
        try{

            String sql = "select disco.titolo as d_titolo,disco.anno,traccia.titolo as t_titolo,traccia.durata, autore.nome_arte "
                       + "from disco, tracce_disco as t_d, traccia, autore "
                       + "where disco.id = t_d.id_disco and traccia.id = t_d.id_traccia "
                       + "and disco.id_autore = autore.id and disco.titolo = ?;";
			
            connection = dataSource.getConnection();
			
	    preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,disco);
                       
            ResultSet rs = preparedStatement.executeQuery();
            
            Disco result = new Disco();
            List<Traccia> tracce = new ArrayList();
            Autore autore;
            Traccia traccia;
            int i = 0;
            while (rs.next()) {
                if(i == 0){
                    result.setTitolo(rs.getString("d_titolo"));
                    result.setAnno(rs.getString("anno"));
                    autore = new Autore("","",rs.getString("nome_arte"));
                    result.setAutore(autore);
                }
                i++;
                traccia = new Traccia(rs.getString("t_titolo"), rs.getInt("durata"));
                tracce.add(traccia);
            }
            result.setTracce(tracce);
            if(i >= 2)
                return new DiscoResources(result);
            else return null;
        }
        catch(Exception e){
           throw new RESTWebApplicationException(e);
        }
    }
    
}
