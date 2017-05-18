package com.chavez.eduardo.recyclerview;

import java.io.Serializable;

/**
 * Created by Eduardo_Chavez on 25/2/2017.
 */

public class Event implements Serializable {
    private String eventName;
    private double eventProgress;
    private String eventDescription;
    private String eventDate;
    private int id;

    public Event(int id,String eventName, double eventProgress, String eventDescription, String eventDate) {
        this.id = id;
        this.eventName = eventName;
        this.eventProgress = eventProgress;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getEventProgress() {
        return eventProgress;
    }

    public void setEventProgress(double eventProgress) {
        this.eventProgress = eventProgress;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
