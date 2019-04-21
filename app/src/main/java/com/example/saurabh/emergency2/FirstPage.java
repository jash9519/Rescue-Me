package com.example.saurabh.emergency2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstPage extends AppCompatActivity {

    AccessingFirebase af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        af=new AccessingFirebase();

        askUserToSwitchOnLocation();

        askPermissionToAccessDeviceLocation();

    }


    private void startTrackerService() {



        startTracking();
        buildnoti();

        //Notify the user that tracking has been enabled
        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_LONG).show();

    }


    private void askUserToSwitchOnLocation() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Toast.makeText(FirstPage.this,"GPS is already on",Toast.LENGTH_SHORT).show();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //GPS is off. Show the user a dialog to upgrade location settings
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(FirstPage.this, 100 /*REQUEST_CHECK_SETTINGS*/);

                        } catch (IntentSender.SendIntentException e) {
                            Toast.makeText(FirstPage.this, "PendingIntent unable to execute request.", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(FirstPage.this, "Location settings are inadequate, and cannot be fixed here. Switch on Location manually", Toast.LENGTH_LONG).show();
                        break;
                }

            }

        });


    }


    public void askPermissionToAccessDeviceLocation() {

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(FirstPage.this, "Permission to access device location already there so start tracking directly", Toast.LENGTH_SHORT).show();

            startTrackerService();

        } else
            // Show the dialog by calling requestPermission and check the result in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 77);


    }


    //Gets triggered as soon as gps is enabled or disabled
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int userdenied = 0;
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) ;
                //GPS SWITCHED ON
            else
                //USER DENIED TO SWITCH ON GPS
                userdenied = 1;
        }

    }

    //Gets triggered as soon as permission is granted to access the device location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == 77 && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //permission granted by user
            Toast.makeText(this, "start tracking", Toast.LENGTH_SHORT).show();
            startTrackerService();

        } else {

            //If the user denies the permission request, then display a toast with some more information//
            Toast.makeText(this, "Please enable location services for this app to allow GPS tracking", Toast.LENGTH_LONG).show();
        }

    }

    public void startTracking()
    {
        Toast.makeText(this,"inside start tracking", Toast.LENGTH_SHORT).show();


        LocationManager lm= (LocationManager) getSystemService(LOCATION_SERVICE);


        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If the app currently has access to the location permission...//
        if (permission == PackageManager.PERMISSION_GRANTED) {


            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Toast.makeText(TrackingService.this,location.toString(), Toast.LENGTH_LONG).show();
                    af.addLocationToFirebase(location);

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
            });

        }
        else
            Toast.makeText(FirstPage.this,"Permission was not given", Toast.LENGTH_LONG).show();

    }

    public void buildnoti()
    {

        TempInitializer ti=new TempInitializer();
        String s=ti.getlatilongi()[0]+" "+ti.getlatilongi()[1];
        Notification noti = new Notification.Builder(this)
                //VALUS HARDCODED FOR CHECKING
        .setContentTitle("HELP!!").setContentText("19.1666139, 72.8548609")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build();

        Toast.makeText(this,"yoooooo", Toast.LENGTH_SHORT).show();

        NotificationManager nm=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(561,noti);

    }

}

