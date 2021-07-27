/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;
import org.univaq.swa.collectors.collectorsrest.security.Logged;

/**
 *
 * @author federicocantoro
 */
public class CollezioneResources {

    DataSource dataSource = null;

    private final Collezione collezione;

    CollezioneResources(Collezione collezione) {
        this.collezione = collezione;
    }

    @GET
    @Produces("application/json")
    public Response getCollezione() {
        try {
            return Response.ok(collezione).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }

    @Logged
    @GET
    @Path("stats")
    @Produces("application/json")
    public Response getStats(
            @PathParam("titolo") String titolo,
            @javax.ws.rs.core.Context ContainerRequestContext req
    ) {
        String username = req.getProperty("username").toString();
       
        try {
            javax.naming.Context initContext = new InitialContext();
            javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Map<String, String> statistiche = new LinkedHashMap();

        try {

            String sql = "select DISTINCT collezione.id from collezione, utente, collezione_condivisa "
                    + "as coll_cond where titolo = ? and ((coll_cond.id_collezione = collezione.id and "
                    + "coll_cond.id_utente = utente.id) OR (collezione.id_utente = utente.id)) "
                    + "and utente.username = ?;";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titolo);
            preparedStatement.setString(2, username);
            ResultSet rs = preparedStatement.executeQuery();

            int idCollezione = -1;
            while (rs.next()) {
                idCollezione = rs.getInt("id");
            }

            if (idCollezione < 0) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } else {
                sql = "select COUNT(id_collezione) as numDischi from dischi_collezione where id_collezione = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idCollezione);
                rs = preparedStatement.executeQuery();
               
                int numDischi = 0;
                while (rs.next()) {
                    numDischi = rs.getInt("numDischi");
                }
                
                statistiche.put("Numero di dischi", Integer.toString(numDischi));
                
                sql = "select COUNT(id_traccia) as numTracce from dischi_collezione, tracce_disco where "
                    + "dischi_collezione.id_disco = tracce_disco.id_disco " +
                      "and dischi_collezione.id_collezione = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idCollezione);
                rs = preparedStatement.executeQuery();
               
                int numTracce = 0;
                while (rs.next()) {
                    numTracce = rs.getInt("numTracce");
                }
                
                statistiche.put("Numero di tracce", Integer.toString(numTracce));
                
                sql = "select SUM(durata) as durata from dischi_collezione, tracce_disco, traccia where "
                    + "dischi_collezione.id_disco = tracce_disco.id_disco " +
                      "and dischi_collezione.id_collezione = ? and tracce_disco.id_traccia = traccia.id";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idCollezione);
                rs = preparedStatement.executeQuery();
               
                int durataTotSecondi = 0;
                while (rs.next()) {
                    durataTotSecondi = rs.getInt("durata");
                }
                
                int ore = durataTotSecondi/ 3600;
                int minuti = durataTotSecondi/60;
                
                String durata = Integer.toString(ore) + "h " + Integer.toString(minuti) +"min";
                statistiche.put("Durata totale", durata);
                
                return Response.ok(statistiche).build();

            }

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }

}
