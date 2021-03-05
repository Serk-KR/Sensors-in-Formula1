/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

import java.io.File;

/**
 *
 * @author Sergi
 */
public class SensorFileReader implements Runnable {

    private File file;

    public SensorFileReader(String filePath) {
        file = new File(filePath);
    }

    @Override
    public void run() {
        // TODO - READ DATA FILE
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
