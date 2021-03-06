/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

import com.sergiboadas.sensors.in.formula1.aggregator.SensorsDataAggregator;
import com.sergiboadas.sensors.in.formula1.model.SensorData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Sergi
 */
public class SensorFileReader implements Runnable {

    private SensorsDataAggregator aggregator;
    private File file;
    private boolean stopReadingFile = false;
    private Semaphore mutex;

    public SensorFileReader(Semaphore mutex, SensorsDataAggregator aggregator, String filePath) {
        this.mutex = mutex;
        this.aggregator = aggregator;
        this.file = new File(filePath);
    }

    @Override
    public void run() {
        SensorData sensorData;
        String line;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            while (!stopReadingFile) {
                line = reader.readLine();
                if (line == null) {
                    Thread.sleep(3000);
//                    System.out.println("Thread sleep - 3s");
                    continue;
                }
                System.out.println("Thread: " + Thread.currentThread().getName() + " | File: " + file.getName() + " | Line: " + line);
                sensorData = processLine(line);
                mutex.acquire();
                aggregator.addSensorData(sensorData);
                mutex.release();
            }
            reader.close();
            System.out.println("Thread: " + Thread.currentThread().getName() + " | Close the file: " + file.getName());

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

    private SensorData processLine(String line) {
        String[] values = line.split(",");
        return new SensorData(Long.parseLong(values[0]), Float.parseFloat(values[1]));
    }
}
