package com.example.saurabh.emergency2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SignupActivity extends AppCompatActivity {

    static EditText firstname,lastname,email,password,confirmpassword,phonenumber;

    static CheckBox cb;
    AccessingFirebase fba;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirmpassword);
        phonenumber=findViewById(R.id.phonenumber);
        cb=findViewById(R.id.checkBox);

        email.setText("");
        password.setText("");

        mAuth = FirebaseAuth.getInstance();

    }

    public void signUp2(View v)
    {

        if(email.getText().toString().length()==0)
        {
            email.setError("Field cannot be left blank");
            email.requestFocus();
        }
        if(password.getText().toString().length()==0)
            password.setError("Field cannot be left blank");
        if(confirmpassword.getText().toString().length()==0)
            confirmpassword.setError("Field cannot be left blank");

        else if (password.getText().toString().equals(confirmpassword.getText().toString()) == false) {
            Toast.makeText(this, "Your password and confirm password do not match", Toast.LENGTH_LONG).show();
            this.confirmpassword.setText("");
            this.confirmpassword.requestFocus();

        }
        else
        {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignupActivity.this, "User registered successfully",Toast.LENGTH_LONG).show();

                        final Users u=new Users();
                        u.setFirstname(SignupActivity.firstname.getText().toString());
                        u.setLastname(SignupActivity.lastname.getText().toString());
                        u.setEmail(SignupActivity.email.getText().toString());
                        u.setPassword(SignupActivity.password.getText().toString());
                        u.setPhonenumber(SignupActivity.phonenumber.getText().toString());

                        if(cb.isChecked()) u.setDoctor("yes");
                        else u.setDoctor("No");


                        //----------------------------------------------------------------------------
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }

                                        // Get new Instance ID token
                                        String token = task.getResult().getToken();

                                        u.token=token;
                                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                                    }
                                });
                        //----------------------------------------------------------------------------------


                        fba=new AccessingFirebase();
                        fba.addObjectToFirebase(u);

                        Intent in = new Intent(SignupActivity.this, FirstPage.class);
                        startActivityForResult(in, 007);

                        //TODO sign up ke baad wale page ka code

                    }
                    else {

                        try {
                            throw task.getException();
                        }
                        catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignupActivity.this, "Email already in use for other account",Toast.LENGTH_LONG).show();
                            SignupActivity.email.setText("");
                        }
                        catch(FirebaseAuthWeakPasswordException e) {
                            SignupActivity.password.setError(e.getReason());
                            SignupActivity.password.requestFocus();
                        }
                        catch (FirebaseNetworkException e){
                            Toast.makeText(SignupActivity.this, "No internet connection",Toast.LENGTH_LONG).show();
                        }

                         catch(Exception e) {
                             Toast.makeText(SignupActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }

                }
            });

        }


    }


}
