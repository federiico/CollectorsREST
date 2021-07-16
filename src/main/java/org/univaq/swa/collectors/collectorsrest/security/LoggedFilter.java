/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.security;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import org.univaq.swa.collectors.collectorsrest.RESTWebApplicationException;

/**
 *
 * @author federicocantoro
 */
@Provider
@Logged
@Priority(Priorities.AUTHENTICATION)
public class LoggedFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;
    DataSource dataSource = null;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = null;
       
        //come esempio, proviamo a cercare il token in vari punti, in ordine di priorità
        //in un'applicazione reale, potremmo scegliere una sola modalità
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null) {
            token = authorizationHeader.substring("Bearer".length()).trim();
        }
        
        
        if (token != null && !token.isEmpty()) {
            try {
                //validiamo il token
                final String username = validateToken(token);
                if (username != null) {
                   
                    requestContext.setProperty("token", token);
                    requestContext.setProperty("username", username);

                } else {
                    //se non va bene... 
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private String validateToken(String token) {
      
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
            
           

            String sql = "select * from utente where token = ?";

            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, token);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return rs.getString("username");
            }
           
            return null;
           
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }
//      
    }

}
