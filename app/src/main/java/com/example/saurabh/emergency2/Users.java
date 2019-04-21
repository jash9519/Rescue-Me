package com.example.saurabh.emergency2;

public class Users
{
    int userscount=0;
    String id,firstname,lastname,email,password,phonenumber,isDoctor;
    Double latitude,longitude;

    String token="";

    //String trustedcontacts[]=new String[5];

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setDoctor(String isDoctor){ this.isDoctor=isDoctor; }

    public String isDoctor(){ return isDoctor ; }


    /*public String[] getTrustedContacts() {
        return trustedcontacts;
    }

    public void setTrustedContacts(String[] trustedcontacts) {
        this.trustedcontacts = trustedcontacts;
    }*/

    public void setCount(int userscount) {
        this.userscount = userscount;
    }

}
