package com.example.projects.newstorms.Models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public float speed;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
