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

    @Path("{titolo: [a-zA-Z0-9]+}")
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
                    + "from collezione "
                    + "where titolo = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();

            int idCollezione = 0;
            String privacy = null;
            while (rs.next()) {
                privacy = rs.getString("privacy");
                idCollezione = rs.getInt("id");
            }
            result.setPrivacy(privacy);

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
    
}
