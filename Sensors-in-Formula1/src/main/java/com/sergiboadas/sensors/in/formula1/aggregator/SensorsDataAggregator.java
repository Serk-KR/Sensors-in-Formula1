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
    private LinkedBlockingQueue<SensorData> sensorsData;
    
    public SensorsDataAggregator() {
        sensorsData = new LinkedBlockingQueue<>();
    }
    public void addSensorData(SensorData data) {
        sensorsData.add(data);
    }

    public LinkedBlockingQueue<SensorData> getSensorsData() {
        return sensorsData;
    }
    
}
