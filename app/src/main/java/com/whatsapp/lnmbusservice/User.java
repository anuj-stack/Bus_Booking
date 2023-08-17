package com.whatsapp.lnmbusservice;

public class User {

    String Fname;
    String Lname;
    String Gender;
    String Batch;

    public User() {
    }



    public String getFname() {
        return Fname;
    }

    public String getLname() {
        return Lname;
    }

    public String getGender() {
        return Gender;
    }

    public String getBatch() {
        return Batch;
    }

    public User(String fname, String lname, String gender, String batch) {
        this.Fname = fname;
        this.Lname = lname;
        this.Gender = gender;
        this.Batch = batch;
    }
}
