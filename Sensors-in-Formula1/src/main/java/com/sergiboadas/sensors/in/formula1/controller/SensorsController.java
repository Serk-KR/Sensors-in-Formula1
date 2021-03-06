/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.controller;

import com.sergiboadas.sensors.in.formula1.aggregator.SensorsDataAggregator;
import com.sergiboadas.sensors.in.formula1.model.SensorData;
import com.sergiboadas.sensors.in.formula1.util.SensorFileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import java.util.stream.Collectors;

/**
 *
 * @author Sergi
 */
public class SensorsController {

    private ExecutorService executor;
    private List<SensorFileReader> listSensorsReaders;
    private SensorsDataAggregator aggregator;
    private final String outputFileName = "AverageSpeed_";
    private int outputFilesGeneraded = 0;
    private Timer timer;
    private Semaphore mutex;

    public SensorsController(List<String> filesPath) {
        this.aggregator = new SensorsDataAggregator();
        mutex = new Semaphore(1);
        this.listSensorsReaders = createSensorFilesReders(filesPath);

        // Create a thread pool with as many threads as the system where the program has
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private List<SensorFileReader> createSensorFilesReders(List<String> filesPath) {
        return filesPath.stream().map(path -> new SensorFileReader(this.mutex, this.aggregator, path)).collect(Collectors.toList());
    }

    public void readIndefinitelySensorsFiles() {
        this.listSensorsReaders.forEach(sensorReader -> {
            this.executor.execute(sensorReader);
        });

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    writeCsvWithTheResults();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 10000);
    }

    public void stopReadingSensorsFiles() throws FileNotFoundException, InterruptedException {
        this.listSensorsReaders.forEach(sensorReader -> sensorReader.stopReading());  // Stopping reading all the files
        this.timer.cancel();        // Cancel timer
        this.executor.shutdown();   // Shutdown executor
    }

    private void writeCsvWithTheResults() throws FileNotFoundException, InterruptedException {
        mutex.acquire();
        LinkedBlockingQueue<SensorData> results = this.aggregator.getSensorsData();
        mutex.release();
        if (!results.isEmpty()) {
            List<SensorData> resultsWithTheAverage = transformDataToFormatNeeded(results);
            printTheResultsToOutputFile(resultsWithTheAverage);
        }
    }

    private List<SensorData> transformDataToFormatNeeded(LinkedBlockingQueue<SensorData> results) {
        List<SensorData> resultsOrdered = results.stream().sorted(Comparator.comparingLong(SensorData::getTime)).collect(Collectors.toList());

        List<SensorData> resultsWithTheAverage = new LinkedList<>();
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
        
        return resultsWithTheAverage;
    }

    private void printTheResultsToOutputFile(List<SensorData> resultsWithTheAverage) throws FileNotFoundException {
        String fileName = this.outputFileName + this.outputFilesGeneraded + ".csv";
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            resultsWithTheAverage
                    .forEach(data -> writer.println(data.getTime() + "," + data.getSpeed()));
        }
        this.outputFilesGeneraded++;
    }
}
