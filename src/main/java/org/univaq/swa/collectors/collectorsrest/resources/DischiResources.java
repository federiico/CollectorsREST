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
import javax.ws.rs.core.MediaType;
import org.univaq.swa.collectors.collectorsrest.model.Autore;
import org.univaq.swa.collectors.collectorsrest.model.Traccia;
import org.univaq.swa.collectors.collectorsrest.model.Disco;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.model.Utente;

/**
 *
 * @author federicocantoro
 */
@Path("collezioni/{collezione: [a-zA-Z0-9]+}/dischi")
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
    
    @POST
    @Consumes("application/json")
    public Response addDisco(
            Disco disco,
            @PathParam("collezione") String titolo,
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {
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

            String sql = "select * "
                    + "from disco "
                    + "where titolo = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, disco.getTitolo());
            ResultSet rs = preparedStatement.executeQuery();

            int idDisco = -1;
            String privacy = null;
            while (rs.next()) {

                idDisco = rs.getInt("id");
            }

            if (idDisco < 0) {
                sql = "select * from autore where nome_arte = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, disco.getAutore().getNome_arte());
                rs = preparedStatement.executeQuery();
                int idAutore = 0;
                while (rs.next()) {
                    idAutore = rs.getInt("id");
                }

                sql = "insert into disco (id_autore,titolo,anno) "
                        + "values (?,?,?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idAutore);
                preparedStatement.setString(2, disco.getTitolo());
                preparedStatement.setString(3, disco.getAnno());

                preparedStatement.executeUpdate();

                Traccia traccia;

                for (int i = 0; i < disco.getTracce().size(); i++) {

                    sql = "insert into traccia (titolo,durata) "
                            + "values (?,?);";

                    traccia = disco.getTracce().get(i);
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, traccia.getTitolo());
                    preparedStatement.setInt(2, traccia.getDurata());
                    preparedStatement.executeUpdate();

                    sql = "insert into tracce_disco (id_disco,id_traccia) "
                            + "values ( (SELECT MAX(id) FROM disco) , (SELECT MAX(id) FROM traccia) );";

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.executeUpdate();
                }

                sql = "select MAX(id) as max from disco;";
                preparedStatement = connection.prepareStatement(sql);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    idDisco = rs.getInt("max");
                }
            }

            sql = "insert into dischi_collezione (id_collezione,id_disco) VALUES "
                    + "( (select id from collezione where titolo = ?), ?);";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titolo);
            preparedStatement.setInt(2, idDisco);
            preparedStatement.executeUpdate();
            
            String uri = uriinfo.getBaseUriBuilder().path(getClass())
                .path(DischiResources.class, "getDisco")
                .build(titolo, disco.getTitolo()).toString();
            
           
            return Response.status(Response.Status.CREATED).entity(uri).type(MediaType.TEXT_PLAIN).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
    
    
}
