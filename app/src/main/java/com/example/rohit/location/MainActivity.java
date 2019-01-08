package com.example.rohit.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> latitude = new ArrayList<>();
    private ArrayList<String> longitude = new ArrayList<>();
    private LocationManager locationManager;
    private Button button;
    private Location location1;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.addLocationButton);
        recyclerView = findViewById(R.id.recyclerview);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },10);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 1,new myLocationListener());



        intRecyclerview();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(location1==null) {
                    Log.v("GPS-ERROR", "location not found");
                    Toast.makeText(getApplicationContext(),"GPS-ERROR",Toast.LENGTH_SHORT).show();
                }else {
                 longitude.add(String.valueOf(location1.getLongitude()));
                 latitude.add(String.valueOf(location1.getLatitude()));
                 adapter.notifyDataSetChanged();
                }
            }
        });

       boolean s = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_RTT);

        Log.d("wifirtt", "onCreate: "+s);

    }


    private void intRecyclerview() {

        adapter = new RecycleViewAdapter(latitude, longitude, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    class myLocationListener implements LocationListener{


        @Override
        public void onLocationChanged(Location location) {
            location1=location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


}