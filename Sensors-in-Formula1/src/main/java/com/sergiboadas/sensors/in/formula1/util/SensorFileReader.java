/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

import com.sergiboadas.sensors.in.formula1.aggregator.SensorsDataAggregator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 *
 * @author Sergi
 */
public class SensorFileReader implements Runnable {

    private SensorsDataAggregator aggregator;
    private File file;
    private boolean stopReadingFile = false;

    public SensorFileReader(SensorsDataAggregator aggregator, String filePath) {
        this.aggregator = aggregator;
        this.file = new File(filePath);
    }

    @Override
    public void run() {
        String line;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            while (!stopReadingFile) {
                line = reader.readLine();
                System.out.println("Thread: " + Thread.currentThread().getName() + " | File: " + file.getName() + " | Line: " + line);
                if (line == null) {
                    Thread.sleep(3000);
                    System.out.println("Thread sleep - 3s");
                }
            }
            reader.close();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void stopReading() {
        this.stopReadingFile = true;
    }

}
