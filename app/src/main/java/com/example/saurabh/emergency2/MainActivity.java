package com.example.saurabh.emergency2;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;



//TODO see bookmark of shrinking your code on google

public class MainActivity extends AppCompatActivity {

    EditText et1, et2;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO resolve line ka error i.e <View/> in xml
        et1 = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        et1.requestFocus();

        mAuth=FirebaseAuth.getInstance();


    }


    public void login(View v) {



        //Toast.makeText(MainActivity.this, et1.getText().toString(), Toast.LENGTH_LONG).show();

        if(et1.getText().toString().length()==0)
        {
            et1.setError("Field cannot be left blank");
            et1.requestFocus();
        }

        if(et2.getText().toString().length()==0)
            et2.setError("Field cannot be left blank");


        else
        {
            mAuth.signInWithEmailAndPassword(et1.getText().toString(), et2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(MainActivity.this,"Successful login", Toast.LENGTH_LONG).show();
                        //FirebaseUser user = mAuth.getCurrentUser();



                        Intent in = new Intent(MainActivity.this, FirstPage.class);
                        startActivityForResult(in, 007);

                    }
                    else {

                        try{
                            throw task.getException();
                        }catch(Exception e) {

                            if(e.getMessage().equals("The password is invalid or the user does not have a password."))
                            {
                                Toast.makeText(MainActivity.this,"Incorrect password", Toast.LENGTH_LONG).show();
                                et2.setText("");
                            }

                            else
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }

                    }
                });

        }

    }


    public void forgotPassword(View v) {

        Toast.makeText(MainActivity.this,"New password: xyzmw5", Toast.LENGTH_LONG).show();
        //TODO SENDING EMAIL
    }


    public void signup1(View v) {

        Intent in = new Intent(this, SignupActivity.class);
        startActivityForResult(in, 007);
    }



    public void openMap(View v)
    {
        Intent in = new Intent(this, openMaps.class);
        startActivityForResult(in, 75);
    }



}







