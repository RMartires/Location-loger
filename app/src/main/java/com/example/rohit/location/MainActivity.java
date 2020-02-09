package com.example.rohit.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    public Handler uihandler;
    public boolean recivedlocation = true;


    private static final String TAGt = "threadchecck";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 10);


        button = findViewById(R.id.addLocationButton);
        recyclerView = findViewById(R.id.recyclerview);

        intRecyclerview();

        final Mythread mythread = new Mythread();
        mythread.start();


        //addlocation-button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recivedlocation == true) {
                    mythread.getMhandler().sendEmptyMessage(0);
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Previous Location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //end button

        uihandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("uithread", "handleMessage: ");
                adapter.notifyDataSetChanged();
            }
        };


    }

    public Handler getuihandler() {
        return uihandler;
    }

    private void intRecyclerview() {

        adapter = new RecycleViewAdapter(latitude, longitude, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void intializelocaton() {

        //location part
        mfusedlocationprovierclient = LocationServices.getFusedLocationProviderClient(this);
        createLocationrequest();
        Log.d(TAGt, "intializelocaton:" + Looper.myLooper().getThread());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d("location", "onLocationResult: nothing");
                    return;
                } else {
                        //remove the loading
                        longitude.remove(longitude.size() - 1);
                        latitude.remove(latitude.size() - 1);
                        //end remove

                        location = locationResult.getLocations().get(0);
                        longitude.add(String.valueOf(location.getLongitude()));
                        latitude.add(String.valueOf(location.getLatitude()));

                        mfusedlocationprovierclient.removeLocationUpdates(locationCallback);
                        Log.d("remove loc updates", "Success");

                        getuihandler().sendEmptyMessage(0);

                        recivedlocation = true;

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



    public class Mythread extends Thread{
        public Handler mhandler;


        public Handler getMhandler() {
            return mhandler;
        }

        @Override
        public void run() {

            Looper.prepare();


                mhandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        intializelocaton();

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mfusedlocationprovierclient.requestLocationUpdates(locationRequest, locationCallback, null);
                        Log.d("req-location updates", "Success");

                        Log.d(TAGt, "run:" + Looper.myLooper().getThread());

                        //put loading
                        longitude.add("Loading...");
                        latitude.add("Loading...");
                        //end
                        getuihandler().sendEmptyMessage(0);

                        recivedlocation = false;
                    }
                };

            Looper.loop();

        }


    }




    }