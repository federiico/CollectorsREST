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

@Path("autori")
public class AutoriResources {

    DataSource dataSource = null;

    @GET
    @Produces("application/json")
    public Response getAutori(@javax.ws.rs.core.Context UriInfo uriinfo) {
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
                    + "from autore";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            List<String> result = new ArrayList();
            List<Autore> autori = new ArrayList();

            while (rs.next()) {
                Autore autore = new Autore();
                autore.setNome(rs.getString("nome"));
                autore.setCognome(rs.getString("cognome"));
                autore.setNome_arte(rs.getString("nome_arte"));

                autori.add(autore);
            }
            return Response.ok(autori).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }

    @Path("{autore: [a-zA-Z0-9]+}/dischi")
    @GET
    @Produces("application/json")
    public Response getDischiAutore(
            @PathParam("autore") String autore,
            @javax.ws.rs.core.Context UriInfo uriinfo
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

            String sql = "SELECT  d.titolo as titolo, d.anno as anno, a.nome_arte as nome_arte, c.titolo as nome_collezione "
                    + "FROM disco as d join autore as a on (a.id=d.id_autore) "
                    + "join  dischi_collezione as dc on (d.id=dc.id_disco) "
                    + "join collezione as c on (dc.id_collezione = c.id) "
                    + "where c.privacy = 'Pubblica' AND a.nome_arte = ? GROUP BY (titolo)";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, autore);
            ResultSet rs = preparedStatement.executeQuery();

            Map<String, Object> disco;
            List<Map<String, Object>> dischi = new ArrayList();
            while (rs.next()) {
                disco = new LinkedHashMap();
                disco.put("titolo", rs.getString("titolo"));
                disco.put("autore", rs.getString("nome_arte"));
                disco.put("anno", rs.getString("anno"));

                String sql2 = "SELECT c.titolo as nome_collezione "
                        + "FROM disco as d join autore as a on (a.id=d.id_autore) "
                        + "join  dischi_collezione as dc on (d.id=dc.id_disco) "
                        + "join collezione as c on (dc.id_collezione = c.id) "
                        + "where c.privacy = 'Pubblica' AND a.nome_arte = ? and d.titolo = ?";

                connection = dataSource.getConnection();

                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setString(1, autore);
                preparedStatement.setString(2, rs.getString("titolo"));
                ResultSet rs2 = preparedStatement.executeQuery();
                
                List<Map<String,String>> collezioni = new ArrayList();
                Map<String, String> collezione;
                while (rs2.next()) {
                    collezione = new LinkedHashMap();
                    collezione.put("nome", rs.getString("nome_collezione"));
                    String uri =  uriinfo.getBaseUriBuilder().path(CollezioniResources.class).path(CollezioniResources.class, "getCollezione").build(rs2.getString("nome_collezione")).toString();
                    collezione.put("uri",uri);
                    collezioni.add(collezione);
                 }
                
                disco.put("collezioni", collezioni);
                dischi.add(disco);
            }
            return Response.ok(dischi).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
}
