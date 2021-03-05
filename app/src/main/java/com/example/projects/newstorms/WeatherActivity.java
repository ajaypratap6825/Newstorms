package com.example.projects.newstorms;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projects.newstorms.Helpers.GetData;
import com.example.projects.newstorms.Helpers.RetrofitInstance;
import com.example.projects.newstorms.Models.LocationMapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    TextView locationn, date, temp, main, sunrise, sunset, max_temp, min_temp, wind, visibility, humidity, pressure;
    ImageView cloud_grey1, cloud_grey2, cloud_grey3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        locationn = findViewById(R.id.location);
        date = findViewById(R.id.date);
        temp = findViewById(R.id.temp);
        main = findViewById(R.id.main);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        max_temp = findViewById(R.id.max_temp);
        min_temp = findViewById(R.id.min_temp);
        wind = findViewById(R.id.wind);
        visibility = findViewById(R.id.visibility);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);

        cloud_grey1 = findViewById(R.id.cloud_grey1);
        cloud_grey1.setAlpha((float) 0.4);
        cloud_grey2 = findViewById(R.id.cloud_grey2);
        cloud_grey2.setAlpha((float) 0.4);
        cloud_grey3 = findViewById(R.id.cloud_grey3);
        cloud_grey3.setAlpha((float) 0.3);

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

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
                    if (networkInfo != null && networkInfo.isConnected()) {
                        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
                        Call<LocationMapObject> call = getData.getWeatherData(location.getLatitude(),
                                location.getLongitude(), "e6c1638f01a306616f0429126f41b4de");
                        call.enqueue(new Callback<LocationMapObject>() {
                            @Override
                            public void onResponse(Call<LocationMapObject> call, Response<LocationMapObject> response) {
                                locationn.setText(response.body().getName() + "," + response.body().getSys().getCountry());

                                Date datee = new java.util.Date(response.body().getDt()*1000);
                                SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy");
                                String formattedDate = sdf.format(datee);
                                date.setText(formattedDate);

                                temp.setText(Float.toString(response.body().getMain().getTemp())+ "°");
                                main.setText(response.body().getWeather().get(0).getDescription());

                                Date sunrisee = new java.util.Date(response.body().getSys().getSunrise()*1000L);
                                SimpleDateFormat sdff = new java.text.SimpleDateFormat("HH:mm:ss a");
                                String formattedDatee = sdff.format(sunrisee);
                                sunrise.setText(formattedDatee);

                                Date sunsett = new java.util.Date(response.body().getSys().getSunset()*1000L);
                                String formattedDateee = sdff.format(sunsett);
                                sunset.setText(formattedDateee);

                                max_temp.setText(Float.toString(response.body().getMain().getTemp_max())+ "°C");
                                min_temp.setText(Double.toString(response.body().getMain().getTemp_min()) + "°C");
                                wind.setText(Float.toString(response.body().getWind().getSpeed())+ " km");
                                visibility.setText(Integer.toString(response.body().getVisibility())+ " km");
                                humidity.setText(Float.toString(response.body().getMain().getHumidity())+ " %");
                                pressure.setText(Float.toString(response.body().getMain().getPressure())+ " hPa");

                                ObjectAnimator animation1 = ObjectAnimator.ofFloat(cloud_grey1, "translationX", 500f);
                                animation1.setDuration(3000);
                                animation1.setRepeatCount(15);
                                animation1.setRepeatMode(ValueAnimator.REVERSE);
                                animation1.start();

                                ObjectAnimator animation2 = ObjectAnimator.ofFloat(cloud_grey2, "translationX", -400f);
                                animation2.setDuration(3000);
                                animation2.setRepeatCount(15);
                                animation2.setRepeatMode(ValueAnimator.REVERSE);
                                animation2.start();

                                ObjectAnimator animation3 = ObjectAnimator.ofFloat(cloud_grey3, "translationX", -800f);
                                animation3.setDuration(5000);
                                animation3.setRepeatCount(15);
                                animation3.setRepeatMode(ValueAnimator.REVERSE);
                                animation3.start();

                            }
                            @Override
                            public void onFailure(Call<LocationMapObject> call, Throwable t) {
                                Toast.makeText(WeatherActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(this, "No network connection !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                showGPSDisabledAlertToUser();
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