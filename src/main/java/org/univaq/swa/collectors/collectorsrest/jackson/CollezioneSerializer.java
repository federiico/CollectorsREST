/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;

/**
 *
 * @author federicocantoro
 */
public class CollezioneSerializer extends JsonSerializer<Collezione>{

    @Override
    public void serialize(Collezione item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeStringField("titolo", item.getTitolo());
        jgen.writeStringField("privacy", item.getPrivacy());
        jgen.writeObjectField("admin", item.getAdmin());
        jgen.writeObjectField("utenti", item.getUtenti());
        jgen.writeObjectField("dischi", item.getDischi());
        jgen.writeEndObject(); // }
    }
}
