package com.example.projects.newstorms.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projects.newstorms.Models.LocationMapObject;
import com.example.projects.newstorms.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends ArrayAdapter<LocationMapObject> {

    public WeatherAdapter(Context context, List<LocationMapObject> event){
        super(context, 0, event);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.weather_list, parent, false);
        }

        LocationMapObject u = getItem(position);

        ImageView image = v.findViewById(R.id.image);
        TextView location = v.findViewById(R.id.location);
        TextView temp = v.findViewById(R.id.temp);
        TextView main = v.findViewById(R.id.main);
        TextView max = v.findViewById(R.id.max);
        TextView min = v.findViewById(R.id.min);
        TextView sunrise = v.findViewById(R.id.sunrise);
        TextView sunset = v.findViewById(R.id.sunset);
        TextView cloudiness= v.findViewById(R.id.cloudiness);
        TextView feelTemp= v.findViewById(R.id.feelTemp);
        TextView wind= v.findViewById(R.id.wind);
        TextView visibility= v.findViewById(R.id.visibility);
        TextView humidity= v.findViewById(R.id.humidity);
        TextView pressure= v.findViewById(R.id.pressure);

        location.setText(u.getName() +","+ u.getCountry());
        String t = Double.toString(u.getTemp());
        temp.setText(t+ "°C");
        main.setText(u.getMain());
        String tmax = Double.toString(u.getTemp_max());
        max.setText(tmax);
        String tmin = Double.toString(u.getTemp_min());
        min.setText(tmin+ "°C");
        String ftemp = Double.toString(u.getFeels_like());
        feelTemp.setText(ftemp+ "°C");

        Date date = new java.util.Date(u.getSunrise()*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(" HH:mm:ss a");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        sunrise.setText(formattedDate);

        Date datee = new java.util.Date(u.getSunset()*1000L);
        SimpleDateFormat sdff = new java.text.SimpleDateFormat(" HH:mm:ss a");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String formattedDatee = sdff.format(datee);
        sunset.setText(formattedDatee);

        String c = Double.toString(u.getAll());
        cloudiness.setText(c+ " %");
        String w = Double.toString(u.getSpeed());
        wind.setText(w+ " km/h");
        String vi = Double.toString(u.getVisibility()/1000);
        visibility.setText(vi+ " km");
        String h = Double.toString(u.getHumidity());
        humidity.setText(h+ " %");
        String p = Double.toString(u.getPressure());
        pressure.setText(p+ " hPa");

        String m = u.getMain();

        if(m.equals("Clear")){
            image.setBackgroundResource(R.drawable.clear);
        }else if ( m.equals("Haze")){
            image.setBackgroundResource(R.drawable.haze);
        }else if (m.equals("Cloudy")){
            image.setBackgroundResource(R.drawable.cloudy);
        }else if (m.equals("Overcast")){
            image.setBackgroundResource(R.drawable.overcast);
        }else if (m.equals("Rain")){
            image.setBackgroundResource(R.drawable.rain);
        }else if (m.equals("Snow")){
            image.setBackgroundResource(R.drawable.snowflake);
        }else if (m.equals("Storm")){
            image.setBackgroundResource(R.drawable.storm);
        }else{
            image.setBackgroundResource(R.drawable.clear);
        }

        return v;
    }
}
