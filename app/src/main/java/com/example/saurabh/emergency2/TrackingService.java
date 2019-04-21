package com.example.saurabh.emergency2;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.location.Location;
import android.content.pm.PackageManager;
import android.widget.Toast;


public class TrackingService extends AppCompatActivity {

    AccessingFirebase af;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tracking_service);

        af=new AccessingFirebase();

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
            Toast.makeText(TrackingService.this,"Permission was not given", Toast.LENGTH_LONG).show();

    }
}


//CODE BELOW NOT WORKING
/*public class TrackingService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AccessingFirebase AF = new AccessingFirebase();
        //Toast.makeText(TrackingService.this, "inside oncreate of trackingservice", Toast.LENGTH_SHORT).show();

        startTracking();
        //buildNotification();

    }

    public void startTracking() {

        final AccessingFirebase af = new AccessingFirebase();

        LocationRequest request = new LocationRequest();

        //Specify how often your app should request the device’s location//
        request.setInterval(10000);

        //Get the most accurate location data available
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If the app currently has access to the location permission...//
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    if (location != null) {

                        af.addLocationToFirebase(location);
                        //Toast.makeText(TrackingService.this, location.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }, null);
        } else
            //This can never happen because we are checking for the permission before starting the tracking service
            Toast.makeText(TrackingService.this, "This can never happen", Toast.LENGTH_SHORT).show();

    }

}*/


/*

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the persistent notification//
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))

        //Make this notification ongoing so it can’t be dismissed by the user//
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.tracking_enabled);
        startForeground(1, builder.build());
    }



    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//Unregister the BroadcastReceiver when the notification is tapped//

            unregisterReceiver(stopReceiver);

//Stop the Service//

            stopSelf();
        }
    };

}

*/