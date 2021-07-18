/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.univaq.swa.collectors.collectorsrest.model.Disco;
import org.univaq.swa.collectors.collectorsrest.model.Utente;
/**
 *
 * @author federicocantoro
 */
public class UtenteSerializer extends JsonSerializer<Utente>{
    
    @Override
    public void serialize(Utente item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeStringField("username", item.getUsername());
        jgen.writeEndObject(); // }
    }
    
}
