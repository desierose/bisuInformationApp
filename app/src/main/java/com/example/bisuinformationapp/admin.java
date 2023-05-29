package com.example.bisuinformationapp;

public class admin{
    public String eventID;
    public String eventName;
    public String date;
    public String location;
    public String description;
    public String fileId;


    public admin() {
    }

    public admin( String eventID, String eventName, String date, String location,String description,String fileId){

        this.eventID = eventID;
        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.description= description;
        this.fileId= fileId;

    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public String getFileId() { return fileId;}
}
