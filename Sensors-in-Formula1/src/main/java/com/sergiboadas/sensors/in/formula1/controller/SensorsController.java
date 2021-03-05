/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.controller;

import com.sergiboadas.sensors.in.formula1.util.SensorFileReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 *
 * @author Sergi
 */
public class SensorsController {
    
    private ExecutorService executor;
    private List<SensorFileReader> listSensors;
    
    public SensorsController(List<String> filesPath) {
        listSensors = createSensorFilesReders(filesPath);
        
        // Create a thread pool with as many threads as the system where the program has
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private List<SensorFileReader> createSensorFilesReders(List<String> filesPath) {
        return filesPath.stream().map(path -> new SensorFileReader(path)).collect(Collectors.toList());
    }

    public void readIndefinitelySensors() {
        // TODO -> Exectute all "runs" methods of each sensorFileRead (class who implements interfice Runnable) (multithreading)
    }
}
