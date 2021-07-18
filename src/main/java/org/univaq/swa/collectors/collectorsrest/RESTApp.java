/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.univaq.swa.collectors.collectorsrest.jackson.ObjectMapperContextResolver;
import org.univaq.swa.collectors.collectorsrest.resources.DiscoResources;
import org.univaq.swa.collectors.collectorsrest.resources.DischiResources;
import org.univaq.swa.collectors.collectorsrest.resources.CollezioneResources;
import org.univaq.swa.collectors.collectorsrest.resources.CollezioniResources;
import org.univaq.swa.collectors.collectorsrest.resources.UtenteResources;
import org.univaq.swa.collectors.collectorsrest.security.AppExceptionMapper;
import org.univaq.swa.collectors.collectorsrest.security.AutenticazioneResource;
import org.univaq.swa.collectors.collectorsrest.security.CORSFilter;
import org.univaq.swa.collectors.collectorsrest.security.LoggedFilter;

/**
 *
 * @author federicocantoro
 */
@ApplicationPath("rest")
public class RESTApp extends Application{
    
        private final Set<Class<?>> classes;

    public RESTApp() {
        HashSet<Class<?>> c = new HashSet<>();
        //aggiungiamo tutte le *root resurces* (cioè quelle
        //con l'annotazione Path) che vogliamo pubblicare
        c.add(AutenticazioneResource.class);
        c.add(DiscoResources.class);
        c.add(DischiResources.class);
        c.add(UtenteResources.class);
        c.add(CollezioneResources.class);
        c.add(CollezioniResources.class);
     

        //aggiungiamo il provider Jackson per poter
        //usare i suoi servizi di serializzazione e 
        //deserializzazione JSON
        c.add(JacksonJsonProvider.class);

        //necessario se vogliamo una (de)serializzazione custom di qualche classe    
        c.add(ObjectMapperContextResolver.class);

        //esempio di autenticazione
        c.add(LoggedFilter.class);

        //aggiungiamo il filtro che gestisce gli header CORS
        c.add(CORSFilter.class);

        //esempio di exception mapper, che mappa in Response eccezioni non già derivanti da WebApplicationException
        c.add(AppExceptionMapper.class);

        classes = Collections.unmodifiableSet(c);
    }

    //l'override di questo metodo deve restituire il set
    //di classi che Jersey utilizzerà per pubblicare il
    //servizio. Tutte le altre, anche se annotate, verranno
    //IGNORATE
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
