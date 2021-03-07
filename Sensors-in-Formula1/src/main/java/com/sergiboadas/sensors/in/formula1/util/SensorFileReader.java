/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

import com.sergiboadas.sensors.in.formula1.aggregator.SensorsDataAggregator;
import com.sergiboadas.sensors.in.formula1.exceptions.SensorDataFileFormatException;
import com.sergiboadas.sensors.in.formula1.model.SensorData;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Sergi
 */
public class SensorFileReader implements Runnable {

    private final SensorsDataAggregator aggregator;
    private final File file;
    private boolean stopReadingFile = false;
    private final Semaphore mutex;

    public SensorFileReader(Semaphore mutex, SensorsDataAggregator aggregator, String filePath) {
        this.mutex = mutex;
        this.aggregator = aggregator;
        this.file = new File(filePath);
    }

    /**
     * Override method of Runnable interface that read the file (stored at 
     * this class), until the stopReadingFile will be set to true
     */
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
                    continue;
                }
                sensorData = processLine(line);
                mutex.acquire();
                aggregator.addSensorData(sensorData);
                mutex.release(); 
            }
            reader.close();

        } catch (InterruptedException | IOException | SensorDataFileFormatException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Makes the file that it has been reading, stop it
     */
    public void stopReading() {
        this.stopReadingFile = true;
    }

    private SensorData processLine(String line) throws SensorDataFileFormatException {
        String[] values = line.split(",");
        if (values.length != 2) {
            throw new SensorDataFileFormatException("All the lines of the sensor data, has to contain a long (time) concatenated with a \",\" and concatenated with a decimal number which represents the speed");
        }
        return new SensorData(Long.parseLong(values[0]), Float.parseFloat(values[1]));
    }
}
