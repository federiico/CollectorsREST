/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;
import org.univaq.swa.collectors.collectorsrest.model.Utente;

/**
 *
 * @author federicocantoro
 */
@Path("auth")
public class AutenticazioneResource {

    DataSource dataSource = null;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doLogin(
            @Context UriInfo uriinfo,
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {
            if (authenticate(username, password)) {

                String authToken = issueToken(uriinfo, username);

                try {
                    javax.naming.Context initContext = new InitialContext();
                    javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                    dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
                } catch (Exception e) {
                    throw new RESTWebApplicationException(e);
                }

                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {

                    String sql = "update utente set token = ? where username = ?";

                    connection = dataSource.getConnection();

                    preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setString(1, authToken);
                    preparedStatement.setString(2, username);

                    int res = preparedStatement.executeUpdate();

                } catch (Exception e) {
                    throw new RESTWebApplicationException(e);
                }
                

                return Response.ok(authToken).header(HttpHeaders.AUTHORIZATION, authToken).build();

            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Logged
    @DELETE
    @Path("/logout")
    public Response doLogout(@Context HttpServletRequest request) {
        try {
            //estraiamo i dati inseriti dal nostro LoggedFilter...
            String token = (String) request.getAttribute("token");
            String username = (String) request.getAttribute("username");
            if (token != null) {
                revokeToken(token, username);
            }
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private boolean authenticate(String username, String password) {

        Utente utente = null;

        try {
            javax.naming.Context initContext = new InitialContext();
            javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            String sql = "select * from utente where username = ? and password = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                utente = new Utente();
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));

            }
            if (utente != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
    }

    private String issueToken(UriInfo context, String username) {

        String token = UUID.randomUUID().toString();
        return token;
    }

    private void revokeToken(String token, String username) {

        try {
            javax.naming.Context initContext = new InitialContext();
            javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CollectorsREST");
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            String sql = "update utente set token = '' where username = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            int res = preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }
}
