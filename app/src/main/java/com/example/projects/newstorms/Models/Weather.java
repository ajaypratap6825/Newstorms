package com.example.projects.newstorms.Models;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
