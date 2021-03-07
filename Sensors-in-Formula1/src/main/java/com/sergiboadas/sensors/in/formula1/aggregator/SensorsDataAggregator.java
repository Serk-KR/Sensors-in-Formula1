/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.aggregator;

import com.sergiboadas.sensors.in.formula1.model.SensorData;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Sergi
 */
public class SensorsDataAggregator {

    private final LinkedBlockingQueue<SensorData> sensorsData; //Queue for storing all the sensors data
    private boolean newDataAdded; //Indicate if exist some new information or not

    public SensorsDataAggregator() {
        sensorsData = new LinkedBlockingQueue<>();
        newDataAdded = false;
    }

    public void addSensorData(SensorData data) {
        sensorsData.add(data);
        newDataAdded = true;
    }

    public LinkedBlockingQueue<SensorData> getSensorsData() {
        return sensorsData;
    }

    /**
     * Returns if exists some new data read
     *
     * @param resourcesPath the location of the resources folder
     * @return if exists some new data
     */
    public boolean isNewDataAdded() {
        return newDataAdded;
    }

    /**
     * Resorte the variable to false in order to indicate that at this 
     * point don't exists new information (right now)
     *
     */
    public void resetNewDataAdded() {
        newDataAdded = false;
    }
}
