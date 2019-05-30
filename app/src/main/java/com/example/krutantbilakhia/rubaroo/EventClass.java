package com.example.krutantbilakhia.rubaroo;

public class EventClass {
    private String eventName;
    private String eventTime;


    EventClass() {
    }

    EventClass(String eventName, String eventTime) {
        this.eventName = eventName;
        this.eventTime = eventTime;
    }

    String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

}

