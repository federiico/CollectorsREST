/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.security;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.univaq.swa.collectors.collectorsrest.RESTCustomException;


/**
 *
 * @author federicocantoro
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<RESTCustomException>{
    
     @Override
    public Response toResponse(RESTCustomException exception) {
        //possiamo trasformare queste eccezioni in una risposta formattata, se non le catturiamo all'origine...
        return Response.serverError().entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
    }
}
