package com.example.rohit.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> latitude = new ArrayList<>();
    private ArrayList<String> longitude = new ArrayList<>();
    private Button button;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mfusedlocationprovierclient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationCallback locationCallback;
    private Location location;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.addLocationButton);
        recyclerView = findViewById(R.id.recyclerview);

        intializelocaton();
        intRecyclerview();

        //addlocation-button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },10);
                    return;
                }
                mfusedlocationprovierclient.requestLocationUpdates(locationRequest,locationCallback,null);
                Log.d("req-location updates", "Success");

            }
        });


    }


    private void intRecyclerview() {

        adapter = new RecycleViewAdapter(latitude, longitude, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void intializelocaton(){

        //location part
        mfusedlocationprovierclient = LocationServices.getFusedLocationProviderClient(this);
        createLocationrequest();

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null){
                    Log.d("location", "onLocationResult: nothing");
                    return;
                }
                else {
                    location=locationResult.getLocations().get(0);
                    longitude.add(String.valueOf(location.getLongitude()));
                    latitude.add(String.valueOf(location.getLatitude()));
                    adapter.notifyDataSetChanged();

                    mfusedlocationprovierclient.removeLocationUpdates(locationCallback);
                    Log.d("remove loc updates", "Success");
                }

            }
        };

    }

    private void createLocationrequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
       // locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



}