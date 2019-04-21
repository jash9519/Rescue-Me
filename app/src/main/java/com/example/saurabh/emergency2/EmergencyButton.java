package com.example.saurabh.emergency2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;



public class EmergencyButton extends AppCompatActivity {


    AccessingFirebase af;
    double myLatLong[];
    double square[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_button);

        af=new AccessingFirebase();
        myLatLong=af.getMyDeviceLocation();

        square=new double[4];

        square[0]=myLatLong[0] + 1/100;
        square[1]=myLatLong[0] - 1/100;
        square[2]=myLatLong[1] + 1/100;
        square[3]=myLatLong[1] - 1/100;

        sendLocation();

    }

    public void sendLocation()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (!task.isSuccessful()) {

                            af.searchAndSend(square);

                        }
                    }
                });

    }


}

