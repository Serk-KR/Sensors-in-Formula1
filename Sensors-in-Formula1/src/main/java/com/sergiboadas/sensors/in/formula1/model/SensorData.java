/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.model;

/**
 *
 * @author Sergi
 */
public class SensorData {

    private Long time;
    private Float speed;

    public SensorData(Long time, Float speed) {
        this.time = time;
        this.speed = speed;
    }
    
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "SensorData{" + "time=" + time + ", speed=" + speed + '}';
    }
    
    public Float getAverage (Float speed2) {
        return (this.speed + speed) / 2;
    }
}
