package com.example.krutantbilakhia.rubaroo;

public class AttendeeClass {
    private String name, status, club;


    AttendeeClass() {
    }

    public AttendeeClass(String name, String club, String status) {
        this.name = name;
        this.club = club;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
