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
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private TimerTask timerTask;
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
        this.timerTask = new TimerTask() {
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
        };
        this.timer = new Timer();
        this.timer.schedule(timerTask, 0, 10000);
    }

    public void stopReadingSensorsFiles() throws FileNotFoundException, InterruptedException {
        System.out.println("stopReadingSensorsFiles");
        this.listSensorsReaders.forEach(sensorReader -> sensorReader.stopReading());
        System.out.println("Cancel timer");
        this.timer.cancel();
        System.out.println("Executor shutdown");
        this.executor.shutdown();
    }

    private void writeCsvWithTheResults() throws FileNotFoundException, InterruptedException {
        String fileName = this.outputFileName + this.outputFilesGeneraded + ".csv";
        System.out.println("Results output file name: " + fileName);
        System.out.println("writeCsvWithTheResults1");
        mutex.acquire();
        System.out.println("writeCsvWithTheResults2");
        LinkedBlockingQueue<SensorData> results = this.aggregator.getSensorsData();
        mutex.release();

        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            results.stream().sorted(Comparator.comparingLong(SensorData::getTime)).forEach(writer::println);
        }
        System.out.println("File generated");
        this.outputFilesGeneraded++;
    }
}
