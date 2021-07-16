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
import org.univaq.swa.collectors.collectorsrest.model.Traccia;

/**
 *
 * @author federicocantoro
 */
public class TracciaSerializer extends JsonSerializer<Traccia>{
    
     @Override
    public void serialize(Traccia item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeStringField("titolo", item.getTitolo());
        int minuti = item.getDurata()/60;
        int secondi = item.getDurata()%60;
        jgen.writeStringField("durata", minuti+":"+secondi);
        jgen.writeEndObject(); // }
    }
    
}
