package com.example.projects.newstorms;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
//import android.app.LoaderManager.LoaderCallbacks;
//import android.content.Loader;
//import android.app.LoaderManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projects.newstorms.Adapters.WeatherAdapter;
import com.example.projects.newstorms.Helpers.QueryUtils;
import com.example.projects.newstorms.Models.LocationMapObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements LocationListener
        /*, LoaderCallbacks<List<LocationMapObject>> */ {
    String apiUrl;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    ListView list;
    private WeatherAdapter adapter;
    //private static final int LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        list = findViewById(R.id.list);
        adapter = new WeatherAdapter(this,new ArrayList<LocationMapObject>());
        list.setAdapter(adapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        list.setEmptyView(mEmptyStateTextView);

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        //LoaderManager loaderManager = getLoaderManager();
        //loaderManager.initLoader(LOADER_ID,null,this);
        AAsyncTask task = new AAsyncTask();

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION);
            Intent i = new Intent(WeatherActivity.this, WeatherActivity.class);
            startActivity(i);
        } else {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" +
                            location.getLongitude() + "&APPID=" + "e6c1638f01a306616f0429126f41b4de" + "&units=metric";
                    if (networkInfo != null && networkInfo.isConnected()) {
                        task.execute(apiUrl);
                    }else {
                        View loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);
                        mEmptyStateTextView.setText("No Internet Connection.");
                    }
                }
            }else {
                showGPSDisabledAlertToUser();
            }
        }
    }
    /*@Override
    public Loader<List<LocationMapObject>> onCreateLoader(int i, Bundle bundle) {
        return new WeatherLoader(this,apiUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<LocationMapObject>> loader, List<LocationMapObject> locationMapObjects) {
        adapter.clear();

        if(locationMapObjects != null && !locationMapObjects.isEmpty()){
            adapter.addAll(locationMapObjects);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<LocationMapObject>> loader) {
        adapter.clear();
    }*/
    private class AAsyncTask extends AsyncTask<String, Void, List<LocationMapObject>> {

        @Override
        protected List<LocationMapObject> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<LocationMapObject> e= QueryUtils.fetchData(urls[0]);
            return e;
        }
        @Override
        protected void onPostExecute(List<LocationMapObject> e) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No weather information.");

            adapter.clear();

            if(e != null && !e.isEmpty()){
                adapter.addAll(e);
            }

        }

    }
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}