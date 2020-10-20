package com.example.projects.newstorms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import com.example.projects.newstorms.Helpers.QueryUtils;
import com.example.projects.newstorms.Models.LocationMapObject;

import java.util.List;

public class WeatherLoader extends AsyncTaskLoader<List<LocationMapObject>> {

    private String mUrl;

    public WeatherLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<LocationMapObject> loadInBackground() {
        if(mUrl == null){

            return null;
        }
        List<LocationMapObject> e= QueryUtils.fetchData(mUrl);
        return e;
    }
}
