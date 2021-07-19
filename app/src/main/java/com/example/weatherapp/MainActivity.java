package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String api_id = "abb1f9020c47a827cda1a3f9b4460716";

    final String WeatherUrl = "https://api.openweathermap.org/data/2.5/weather";
    final long minTime = 5000;
    final float distance = 1000;
    final int REQUEST_CODE = 101;


    String LocationProvider= LocationManager.GPS_PROVIDER;
    TextView nameofcity, weatherState, temperature;
    ImageView weathericon;
    RelativeLayout cityFinder;
    LocationManager locationManager;
    LocationListener locationListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherState = findViewById(R.id.weatherCondition);
        temperature = findViewById(R.id.temperature);
        weathericon = findViewById(R.id.weathericon);
        nameofcity = findViewById(R.id.cityName);
        cityFinder = findViewById(R.id.cityFinder);

        cityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,cityFinder.class);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLcatin();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",api_id);
        letsdosomething(params);

    }


    private void getWeatherForCurrentLcatin() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params=new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",api_id);
                letsdosomething(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {


            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation    0
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationProvider, minTime, distance, locationListener);


         }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"succesful",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLcatin();
            }
            else {

            }
        }
    }
    private void letsdosomething(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WeatherUrl,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "data fetched succesfful", Toast.LENGTH_SHORT).show();
                WeatherData weatherData=WeatherData.fromJson(response);
                updateUi(weatherData);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUi(WeatherData weatherData){
        temperature.setText(weatherData.getmTemperature());
        nameofcity.setText(weatherData.getMcity());
        weatherState.setText(weatherData.getMweathertype());
        int resourseId=getResources().getIdentifier(weatherData.getMicon(),"drawable",getPackageName());
        weathericon.setImageResource(resourseId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}