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
@Path("collezioni")
public class CollezioniResources {

    DataSource dataSource = null;

    @Path("{titolo: [a-zA-Z0-9%20]+}")
    @Produces("application/json")
    public CollezioneResources getCollezione(
            @PathParam("titolo") String titolo,
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
        Collezione result = new Collezione();
        result.setTitolo(titolo);

        try {

            String sql = "select * "
                    + "from collezione, utente "
                    + "where titolo = ? and collezione.id_utente = utente.id";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();

            int idCollezione = 0;
            String privacy = null;
            Utente admin = new Utente();
            while (rs.next()) {
                privacy = rs.getString("privacy");
                idCollezione = rs.getInt("id");
                admin.setUsername(rs.getString("username"));
            }
            result.setPrivacy(privacy);
            result.setAdmin(admin);

            sql = "select * "
                    + "from collezione_condivisa as cond, utente "
                    + "where cond.id_utente = utente.id and cond.id_collezione = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCollezione);
            rs = preparedStatement.executeQuery();

            Utente utente;
            List<Utente> utenti_condivisi = new ArrayList();
            while (rs.next()) {
                utente = new Utente();
                utente.setUsername(rs.getString("username"));
                utenti_condivisi.add(utente);
            }
            result.setUtenti(utenti_condivisi);

            sql = "select * from dischi_collezione as dis_coll, disco, autore where "
                    + "dis_coll.id_collezione = ? and dis_coll.id_disco = disco.id and disco.id_autore = autore.id;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCollezione);
            rs = preparedStatement.executeQuery();

            Map<String, Object> disco;
            List<Map<String, Object>> dischi = new ArrayList();
            while (rs.next()) {
                disco = new LinkedHashMap();
                disco.put("titolo", rs.getString("titolo"));
                disco.put("anno", rs.getString("anno"));
                disco.put("autore", rs.getString("nome_arte"));
                String uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(titolo, rs.getString("titolo")).toString();
                disco.put("uri", uri);
                dischi.add(disco);
            }

            result.setDischi(dischi);

            return new CollezioneResources(result);

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
    
    @GET
    @Produces("application/json")
    public Response getCollezioniPubblicheFiltered(
            @QueryParam("titolo") String titolo,
            @QueryParam("autore") String autore,
            @QueryParam("traccia") String traccia,
            @QueryParam("anno") String anno,
            @javax.ws.rs.core.Context UriInfo uriinfo
    ){
         if (autore == null && anno == null && traccia == null) {
                    return getDischiFromPubblicheByTitolo(titolo, uriinfo);
                }

                if (titolo == null && anno == null && traccia == null) {
                    return getDischiFromPubblicheByAutore(autore, uriinfo);
                }

                if (titolo == null && traccia == null && autore == null) {
                    return getDischiFromPubblicheByAnno(anno, uriinfo);
                }

                if (titolo == null && anno == null && autore == null) {
                    return getDischiFromPubblicheByTraccia(traccia, uriinfo);
                }

                if (titolo == null && traccia == null) {
                    return getDischiFromPubblicheByAutoreAndAnno(autore, anno, uriinfo);
                }
                return null;
    }

    
    @Produces("application/json")
    public Response getDischiFromPubblicheByTitolo(
            @QueryParam("titolo") String titolo,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, dischi_collezione as dis_col, disco where "
                    + "collezione.privacy = 'pubblica' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and disco.titolo = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titolo);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> dischi = new ArrayList<String>();

            while (rs.next()) {

                URI uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(rs.getString("collezione"), rs.getString("disco"));
                dischi.add(uri.toString());
            }
            return Response.ok(dischi).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

    
    @Produces("application/json")
    public Response getDischiFromPubblicheByAutore(
            @QueryParam("autore") String autore,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, dischi_collezione as dis_col, disco, autore where "
                    + "collezione.privacy = 'pubblica' and dis_col.id_collezione = collezione.id and "
                    + "disco.id_autore = autore.id and autore.nome_arte = ? "
                    + "and dis_col.id_disco = disco.id;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, autore);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> dischi = new ArrayList<String>();

            while (rs.next()) {

                URI uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(rs.getString("collezione"), rs.getString("disco"));
                dischi.add(uri.toString());
            }
            return Response.ok(dischi).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

    
    @Produces("application/json")
    public Response getDischiFromPubblicheByAnno(
            @QueryParam("anno") String anno,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, dischi_collezione as dis_col, disco where "
                    + "collezione.privacy = 'pubblica' and dis_col.id_collezione = collezione.id "
                    + "and disco.anno = ? "
                    + "and dis_col.id_disco = disco.id;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, anno);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> dischi = new ArrayList<String>();

            while (rs.next()) {

                URI uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(rs.getString("collezione"), rs.getString("disco"));
                dischi.add(uri.toString());
            }
            return Response.ok(dischi).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

    
    @Produces("application/json")
    public Response getDischiFromPubblicheByTraccia(
            @QueryParam("traccia") String traccia,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, dischi_collezione as dis_col, disco, traccia, tracce_disco as tra_dis "
                    + "where "
                    + "collezione.privacy = 'pubblica' and dis_col.id_collezione = collezione.id and "
                    + "tra_dis.id_disco = disco.id "
                    + "and tra_dis.id_traccia = traccia.id and traccia.titolo = ? "
                    + "and dis_col.id_disco = disco.id;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, traccia);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> dischi = new ArrayList<String>();

            while (rs.next()) {

                URI uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(rs.getString("collezione"), rs.getString("disco"));
                dischi.add(uri.toString());
            }
            return Response.ok(dischi).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

    
    @Produces("application/json")
    public Response getDischiFromPubblicheByAutoreAndAnno(
            @QueryParam("autore") String autore,
            @QueryParam("anno") String anno,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, dischi_collezione as dis_col, disco, autore where "
                    + "collezione.privacy = 'pubblica' and dis_col.id_collezione = collezione.id and "
                    + "disco.id_autore = autore.id "
                    + "and disco.anno = ? and autore.nome_arte = ? "
                    + "and dis_col.id_disco = disco.id;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, anno);
            preparedStatement.setString(2, autore);

            ResultSet rs = preparedStatement.executeQuery();

            List<String> dischi = new ArrayList<String>();

            while (rs.next()) {

                URI uri = uriinfo.getBaseUriBuilder().path(DischiResources.class)
                        .path(DischiResources.class, "getDisco")
                        .build(rs.getString("collezione"), rs.getString("disco"));
                dischi.add(uri.toString());
            }
            return Response.ok(dischi).build();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }
}
