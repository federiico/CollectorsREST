/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.univaq.swa.collectors.collectorsrest.model.Disco;
import org.univaq.swa.collectors.collectorsrest.model.Traccia;
import org.univaq.swa.collectors.collectorsrest.model.Utente;
import org.univaq.swa.collectors.collectorsrest.model.Collezione;


/**
 *
 * @author federicocantoro
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //abilitiamo una feature nuova...
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule customSerializer = new SimpleModule("CustomSerializersModule");
        
        
        customSerializer.addSerializer(Disco.class, new DiscoSerializer());
        customSerializer.addDeserializer(Disco.class, new DiscoDeserializer());
        customSerializer.addSerializer(Traccia.class, new TracciaSerializer());
        customSerializer.addSerializer(Utente.class, new UtenteSerializer());
        customSerializer.addSerializer(Collezione.class, new CollezioneSerializer());
        //configuriamo i nostri serializzatori custom
        //customSerializer.addSerializer(Calendar.class, new JavaCalendarSerializer());
        //customSerializer.addDeserializer(Calendar.class, new JavaCalendarDeserializer());
        //
        //customSerializer.addSerializer(Brano.class, new BranoSerializer());
        //customSerializer.addDeserializer(Fattura.class, new FatturaDeserializer());
        //

        mapper.registerModule(customSerializer);

        return mapper;
    }
    
}
