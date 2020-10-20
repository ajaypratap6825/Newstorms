package com.example.projects.newstorms.Helpers;

import android.text.TextUtils;
import android.util.Log;

import com.example.projects.newstorms.Models.LocationMapObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public QueryUtils() {
    }

    private static String LOG_TAG= QueryUtils.class.getSimpleName();

    public static List<LocationMapObject> fetchData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

        }
        List<LocationMapObject> e = extractFeatureFromJson(jsonResponse);
        return e;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<LocationMapObject> extractFeatureFromJson(String eJSON) {
        if (TextUtils.isEmpty(eJSON)) {
            return null;
        }
        List<LocationMapObject> event= new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(eJSON);
            JSONArray array = baseJsonResponse.getJSONArray("weather");

            JSONObject m = array.getJSONObject(0);
            String main = m.getString("main");


            JSONObject coord = baseJsonResponse.getJSONObject("coord");
            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");

            JSONObject ma = baseJsonResponse.getJSONObject("main");
            Double temp = ma.getDouble("temp");
            Double feels_like = ma.getDouble("feels_like");
            Double temp_max = ma.getDouble("temp_max");
            Double temp_min = ma.getDouble("temp_min");
            Double pressure = ma.getDouble("pressure");
            Double humidity = ma.getDouble("humidity");

            Double visibility = baseJsonResponse.getDouble("visibility");

            JSONObject w = baseJsonResponse.getJSONObject("wind");
            Double speed = w.getDouble("speed");

            JSONObject c = baseJsonResponse.getJSONObject("clouds");
            Double all = c.getDouble("all");

            JSONObject sys = baseJsonResponse.getJSONObject("sys");
            Long sunrise = sys.getLong("sunrise");
            Long sunset = sys.getLong("sunset");
            String country = sys.getString("country");

            String name = baseJsonResponse.getString("name");

            LocationMapObject l = new LocationMapObject(lat,lon,temp,feels_like,temp_max,temp_min,pressure,humidity,visibility,
                    speed,all,main,country,name,sunrise,sunset);
            event.add(l);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the data", e);
        }
        return event;

    }

}
