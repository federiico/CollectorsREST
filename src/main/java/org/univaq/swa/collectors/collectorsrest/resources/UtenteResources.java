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
            @QueryParam("privacy") String privacy,
            @QueryParam("utente") String utente,
            @QueryParam("titolo") String titolo,
            @QueryParam("autore") String autore,
            @QueryParam("anno") String anno,
            @QueryParam("traccia") String traccia,
            @javax.ws.rs.core.Context UriInfo uriinfo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {
        if (privacy.equalsIgnoreCase("personale")) {
            if (utente == null && titolo == null && autore == null && anno == null && traccia == null) {
                return getCollezioniPersonali(uriinfo, req);
            } else {
                if (autore == null && anno == null && traccia == null) {
                    return getCollezioniPersonaliByTitolo(titolo, uriinfo, req);
                }

                if (titolo == null && anno == null && traccia == null) {
                    return getCollezioniPersonaliByAutore(autore, uriinfo, req);
                }

                if (titolo == null && traccia == null && autore == null) {
                    return getCollezioniPersonaliByAnno(anno, uriinfo, req);
                }

                if (titolo == null && anno == null && autore == null) {
                    return getCollezioniPersonaliByTraccia(traccia, uriinfo, req);
                }

                if (titolo == null && traccia == null) {
                    return getCollezioniPersonaliByAnnoAndAutore(autore, anno, uriinfo, req);
                }
                return null;
            }
        } else {
            if (titolo == null && autore == null && anno == null && traccia == null) {
                return getCollezioniCondivise(utente, uriinfo, req);
            } else {
                if (autore == null && anno == null && traccia == null) {
                    return getCollezioniCondiviseByTitolo(utente, titolo, uriinfo, req);
                }

                if (titolo == null && anno == null && traccia == null) {
                    return getCollezioniCondiviseByAutore(utente, autore, uriinfo, req);
                }

                if (titolo == null && traccia == null && autore == null) {
                    return getCollezioniCondiviseByAnno(utente, anno, uriinfo, req);
                }

                if (titolo == null && anno == null && autore == null) {
                    return getCollezioniCondiviseByTraccia(utente, traccia, uriinfo, req);
                }

                if (titolo == null && traccia == null) {
                    return getCollezioniCondiviseByAnnoAndAutore(utente, autore, anno, uriinfo, req);
                }
                return null;

            }
        }
    }

    @Produces("application/json")
    public Response getCollezioniPersonali(
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

            String sql = "SELECT * from collezione, utente, collezione_condivisa as coll_cond where utente.username = ? "
                    + " and coll_cond.id_collezione = collezione.id;";

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

    @Produces("application/json")
    public Response getCollezioniPersonaliByTitolo(
            @QueryParam("titolo") String titolo,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, dischi_collezione as dis_col, disco "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and disco.titolo= ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, titolo);

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
    public Response getCollezioniPersonaliByAutore(
            @QueryParam("autore") String autore,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, dischi_collezione as dis_col, disco, autore "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and disco.id_autore = autore.id and autore.nome_arte = ? ORDER BY collezione.titolo;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
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

    @Produces("application/json")
    public Response getCollezioniPersonaliByAnno(
            @QueryParam("anno") String anno,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, dischi_collezione as dis_col, disco "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and disco.anno= ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, anno);

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
    public Response getCollezioniPersonaliByTraccia(
            @QueryParam("traccia") String traccia,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, dischi_collezione as dis_col, disco, tracce_disco as tra_disc, traccia "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and tra_disc.id_disco = disco.id and tra_disc.id_traccia = traccia.id "
                    + "and traccia.titolo = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, traccia);

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
    public Response getCollezioniPersonaliByAnnoAndAutore(
            @QueryParam("autore") String autore,
            @QueryParam("anno") String anno,
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

            String sql = "select collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, dischi_collezione as dis_col, disco, autore "
                    + "where collezione.id_utente = utente.id and utente.username = ? and "
                    + "collezione.privacy = 'personale' and dis_col.id_collezione = collezione.id "
                    + "and dis_col.id_disco = disco.id and disco.anno = ? and disco.id_autore = autore.id and autore.nome_arte = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, anno);
            preparedStatement.setString(3, autore);

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
    public Response getCollezioniCondiviseByTitolo(
            @QueryParam("utente") String utente,
            @QueryParam("titolo") String titolo,
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

            String sql = "SELECT collezione.titolo as collezione, disco.titolo as disco "
                    + "from collezione, utente, collezione_condivisa as coll_cond, disco, "
                    + "dischi_collezione as dis_coll where utente.username = ? "
                    + "and coll_cond.id_collezione = collezione.id and coll_cond.id_collezione = dis_coll.id_collezione "
                    + "and disco.id = dis_coll.id_disco and disco.titolo = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);
            preparedStatement.setString(2, titolo);

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
    public Response getCollezioniCondiviseByAutore(
            @QueryParam("utente") String utente,
            @QueryParam("autore") String autore,
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

            String sql = "SELECT collezione.titolo as collezione, disco.titolo as disco from collezione, "
                    + "utente, collezione_condivisa as coll_cond, disco, dischi_collezione as dis_coll, autore "
                    + "where utente.username = ? and coll_cond.id_collezione = collezione.id "
                    + "and coll_cond.id_collezione = dis_coll.id_collezione and disco.id = dis_coll.id_disco "
                    + "and disco.id_autore = autore.id and autore.nome_arte = ? order by collezione.titolo;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);
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

    @Produces("application/json")
    public Response getCollezioniCondiviseByAnno(
            @QueryParam("utente") String utente,
            @QueryParam("anno") String anno,
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

            String sql = "SELECT collezione.titolo as collezione, disco.titolo as disco from collezione, "
                    + "utente, collezione_condivisa as coll_cond, disco, dischi_collezione as dis_coll "
                    + "where utente.username = ? and coll_cond.id_collezione = collezione.id "
                    + "and coll_cond.id_collezione = dis_coll.id_collezione and disco.id = dis_coll.id_disco "
                    + "and disco.anno = ? ORDER BY collezione.titolo;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);
            preparedStatement.setString(2, anno);

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
    public Response getCollezioniCondiviseByTraccia(
            @QueryParam("utente") String utente,
            @QueryParam("traccia") String traccia,
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

            String sql = "SELECT collezione.titolo as collezione, disco.titolo as disco from collezione, utente, collezione_condivisa as coll_cond, disco, "
                    + "dischi_collezione as dis_coll, traccia, tracce_disco as tra_dis where utente.username = ? and "
                    + "coll_cond.id_collezione = collezione.id "
                    + "and coll_cond.id_collezione = dis_coll.id_collezione and disco.id = dis_coll.id_disco and tra_dis.id_disco = disco.id "
                    + "and tra_dis.id_traccia = traccia.id and traccia.titolo = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);
            preparedStatement.setString(2, traccia);

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
    public Response getCollezioniCondiviseByAnnoAndAutore(
            @QueryParam("utente") String utente,
            @QueryParam("autore") String autore,
            @QueryParam("anno") String anno,
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

            String sql = "SELECT collezione.titolo as collezione, disco.titolo as disco from collezione, utente, "
                    + "collezione_condivisa as coll_cond, "
                    + "disco, dischi_collezione as dis_coll, autore "
                    + "where utente.username = ? and coll_cond.id_collezione = collezione.id and "
                    + "coll_cond.id_collezione = dis_coll.id_collezione "
                    + "and disco.id = dis_coll.id_disco and disco.anno = ? and "
                    + "disco.id_autore = autore.id and autore.nome_arte = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, utente);
            preparedStatement.setString(2, anno);
            preparedStatement.setString(3, autore);

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
