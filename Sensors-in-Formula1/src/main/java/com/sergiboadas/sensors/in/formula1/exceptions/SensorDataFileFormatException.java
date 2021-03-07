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
public class SensorDataFileFormatException extends Exception {

    public SensorDataFileFormatException(String message) {
        super(message);
    }

    public SensorDataFileFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
