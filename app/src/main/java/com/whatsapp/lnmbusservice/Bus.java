package com.whatsapp.lnmbusservice;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Bus {

    private String id;
    private String arrival;
    private String destination;
    private String time;
    private String seats;

    public Bus() {
    }

    public String getId() {
        return id;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    public String getSeats() {
        return seats;
    }

    public Bus(String Id, String arrival, String destination, String time, String seats) {
        this.id = Id;
        this.arrival = arrival;
        this.destination = destination;
        this.time = time;
        this.seats = seats;
    }
}
