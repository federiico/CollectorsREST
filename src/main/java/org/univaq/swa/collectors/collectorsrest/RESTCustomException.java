/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.univaq.swa.collectors.collectorsrest;

/**
 *
 * @author federicocantoro
 */
public class RESTCustomException extends Exception{
    
    public RESTCustomException() {
    }

    public RESTCustomException(String message) {
        super(message);
    }

    public RESTCustomException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
