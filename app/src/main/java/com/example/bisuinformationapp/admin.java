package com.example.bisuinformationapp;

public class admin{
    public String eventID;
    public String eventName;
    public String date;
    public String location;
    public String description;


    public admin() {
    }

    public admin( String eventID, String eventName, String date, String location,String description){

        this.eventID = eventID;
        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.description= description;

    }
    public String getEventID(){return eventID; }

    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
