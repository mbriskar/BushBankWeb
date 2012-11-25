/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.logging;

/**
 *
 * @author Mato
 */
public class Logger {

    private static Logger instance;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }

    return instance ;
    }
    
    public void log(String text) {
        
    }

    } 