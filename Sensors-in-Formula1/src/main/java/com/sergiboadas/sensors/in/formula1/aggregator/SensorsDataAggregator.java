/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.aggregator;

import com.sergiboadas.sensors.in.formula1.model.SensorData;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sergi
 */
public class SensorsDataAggregator {
    private List<SensorData> sensorsData;
    
    public SensorsDataAggregator() {
        sensorsData = new LinkedList<>();
    }
    public void addSensorData(SensorData data) {
        sensorsData.add(data);
    }

    public List<SensorData> getSensorsData() {
        return sensorsData;
    }
    
}
