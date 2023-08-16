package com.example.notification;

public class DataItem {
    private double temperature;
    private double humidity;
    private double heartBeat;

    public DataItem(double temperature, double humidity, double heartBeat) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.heartBeat = heartBeat;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getHeartBeat() {
        return heartBeat;
    }

}

