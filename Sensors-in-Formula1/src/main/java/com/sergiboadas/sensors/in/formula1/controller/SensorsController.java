/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.controller;

import com.sergiboadas.sensors.in.formula1.aggregator.SensorsDataAggregator;
import com.sergiboadas.sensors.in.formula1.model.SensorData;
import com.sergiboadas.sensors.in.formula1.util.SensorFileReader;
import com.sergiboadas.sensors.in.formula1.util.SensorFileWriter;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Sergi
 */
public class SensorsController {

    private final ExecutorService executor;
    private Timer timer;
    private final Semaphore mutex;
    
    private final SensorsDataAggregator aggregator;
    private final SensorFileWriter printer;
    private final List<SensorFileReader> listSensorsReaders;

    public SensorsController(List<String> filesPath) {
        this.printer = new SensorFileWriter();
        this.aggregator = new SensorsDataAggregator();
        this.mutex = new Semaphore(1);
        this.listSensorsReaders = createSensorFilesReders(filesPath);

        // Create a thread pool with as many threads as the system where the program has
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private List<SensorFileReader> createSensorFilesReders(List<String> filesPath) {
        return filesPath.stream().map(path -> new SensorFileReader(this.mutex, this.aggregator, path)).collect(Collectors.toList());
    }

    /**
     * Read indefenedly all the files stored at the list of SensorFileReaders object
     * and create a task which every 10 seconds will check if there are some new
     * information in at minimum one file. If there is it, then write a csv file
     * with the results
     */
    public void readIndefinitelySensorsFiles() {
        this.listSensorsReaders.forEach(sensorReader -> {
            this.executor.execute(sensorReader);
        });

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (aggregator.isNewDataAdded()) {
                    writeResultsToCsvFile();
                }
            }
        }, 0, 10000);
    }

    /**
     * Returns a list of strings which represents the path of all the regular files
     * with a name stared by "sensor" and with a csv extension
     *
     * @throws FileNotFoundException, InterruptedException
     */
    public void stopReadingSensorsFiles() throws FileNotFoundException, InterruptedException {
        this.listSensorsReaders.forEach(sensorReader -> sensorReader.stopReading());  // Stopping reading all the files
        this.timer.cancel();        // Cancel timer
        this.executor.shutdown();   // Shutdown executor
    }

    private void writeResultsToCsvFile()  {
        try {
            mutex.acquire();
            LinkedBlockingQueue<SensorData> results = this.aggregator.getSensorsData();
            this.aggregator.resetNewDataAdded();
            mutex.release();
            if (!results.isEmpty()) {
                List<SensorData> resultsWithTheAverage = transformDataToFormatNeeded(results);
                printer.printOutputFileCsv(resultsWithTheAverage);
            } 
        } catch (InterruptedException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private List<SensorData> transformDataToFormatNeeded(LinkedBlockingQueue<SensorData> results) {
        List<SensorData> resultsOrdered = results.stream().sorted(Comparator.comparingLong(SensorData::getTime)).collect(Collectors.toList());
        List<SensorData> resultsWithTheAverage = new LinkedList<>();

        if (!resultsOrdered.isEmpty()) {
            SensorData firstData = resultsOrdered.get(0);
            Long timeBefore = firstData.getTime();
            float sumOfSpeeds = 0;
            int numberOfSpeeds = 0;
            for (SensorData data : resultsOrdered) {
                if (!Objects.equals(timeBefore, data.getTime())) {
                    resultsWithTheAverage.add(new SensorData(timeBefore, sumOfSpeeds / numberOfSpeeds));
                    timeBefore = data.getTime();
                    sumOfSpeeds = data.getSpeed();
                    numberOfSpeeds = 1;
                } else {
                    sumOfSpeeds += data.getSpeed();
                    numberOfSpeeds++;
                }
            }
            resultsWithTheAverage.add(new SensorData(timeBefore, sumOfSpeeds / numberOfSpeeds));
        }

        return resultsWithTheAverage;
    }
}
