package com.example.projects.newstorms.Helpers;

import com.example.projects.newstorms.Models.LocationMapObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetData {
    @GET("data/2.5/weather?units=metric")
    Call<LocationMapObject> getWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String app_id);
}
