/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.exceptions;

/**
 *
 * @author Sergi
 */
public class SensorFileNotFoundException extends Exception {

    public SensorFileNotFoundException(String message) {
        super(message);
    }

    public SensorFileNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
