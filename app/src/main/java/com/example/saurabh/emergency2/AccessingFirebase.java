package com.example.saurabh.emergency2;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.os.Message.*;

public class AccessingFirebase
{
    DatabaseReference alluserdetails;

    AccessingFirebase()
    {
        alluserdetails=FirebaseDatabase.getInstance().getReference("alluserdetails");
    }

    public void addObjectToFirebase(final Users u)
    {
        alluserdetails.addValueEventListener(new ValueEventListener()
        {
            int counter=0,flag=1;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userincoating :dataSnapshot.getChildren())
                {
                    counter++;
                    Users user=userincoating.getValue(Users.class);
                    if( (user.getEmail()).equals(u.getEmail()) )
                    {
                        flag=0;
                        break;
                    }
                }

                if(flag==1)
                {
                    u.setCount(++counter);
                    String id = alluserdetails.push().getKey(); //This lets us make new user every time by generating new id each time
                    alluserdetails.child(id).setValue(u);

                    //TODO ??? toast that user added

                }
                else
                    //TODO  ??? toast that user not added
                    ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void addLocationToFirebase(final Location loc)
    {
        FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
        final String CurrentUserKaEmail=u.getEmail();

        alluserdetails.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userincoating :dataSnapshot.getChildren())
                {
                    Users user=userincoating.getValue(Users.class);
                    if( (user.getEmail()).equals(CurrentUserKaEmail) )
                    {
                        DatabaseReference childRef=userincoating.getRef();
                        user.setLatitude(loc.getLatitude());
                        user.setLongitude(loc.getLongitude());
                        childRef.setValue(user);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public double[] getMyDeviceLocation()
    {
        FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
        final String CurrentUserKaEmail=u.getEmail();
        final double[] LatLong = new double[]{100,100};

        alluserdetails.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userincoating :dataSnapshot.getChildren())
                {
                    Users user=userincoating.getValue(Users.class);
                    if( (user.getEmail()).equals(CurrentUserKaEmail) )
                    {
                        DatabaseReference childRef=userincoating.getRef();
                        LatLong[0] =user.getLatitude();
                        LatLong[1] =user.getLongitude();
                        childRef.setValue(user);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return LatLong;

    }



    public double[] searchAndSend(final double[] square)
    {
        final double LatLong[]=new double[2];
        alluserdetails.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userincoating :dataSnapshot.getChildren())
                {
                    Users user=userincoating.getValue(Users.class);
                    if(  user.getLatitude() < square[0]  &&  user.getLatitude() > square[1]
                         && user.getLongitude() < square[2]  &&  user.getLongitude() > square[3]  )
                    {
                        DatabaseReference childRef=userincoating.getRef();
                        LatLong[0] =user.getLatitude();
                        LatLong[1] =user.getLongitude();
                        childRef.setValue(user);
                        break;

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return LatLong;

    }







}
