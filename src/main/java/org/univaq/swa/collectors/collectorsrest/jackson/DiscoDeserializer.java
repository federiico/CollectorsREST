/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.jackson;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.collectors.collectorsrest.model.Disco;
import org.univaq.swa.collectors.collectorsrest.model.Autore;
import org.univaq.swa.collectors.collectorsrest.model.Traccia;


/**
 *
 * @author federicocantoro
 */
public class DiscoDeserializer extends JsonDeserializer<Disco>{
    
    @Override
    public Disco deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Disco disco = new Disco();
        
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("titolo")) {
            disco.setTitolo(node.get("titolo").asText());
        }
        
        if (node.has("anno di uscita")) {
            disco.setAnno(node.get("anno di uscita").asText());
        }

        if (node.has("autore")) {
            disco.setAutore(jp.getCodec().treeToValue(node.get("autore"), Autore.class));
        }
        
        if (node.has("tracce")) {
            JsonNode ne = node.get("tracce");
            List<Traccia> tracce = new ArrayList<>();
            disco.setTracce(tracce);
            for (int i = 0; i < ne.size(); ++i) {
                tracce.add(jp.getCodec().treeToValue(ne.get(i), Traccia.class));
            }           
        }

        return disco;
    }
    
}
