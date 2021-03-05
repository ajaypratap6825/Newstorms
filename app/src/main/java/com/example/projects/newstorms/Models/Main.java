package com.example.projects.newstorms.Models;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    public float temp;
    @SerializedName("temp_min")
    public double temp_min;
    @SerializedName("temp_max")
    public float temp_max;
    @SerializedName("pressure")
    public float pressure;
    @SerializedName("humidity")
    public float humidity;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
