package com.example.krutantbilakhia.rubaroo;

import java.math.BigInteger;

public class UserClass {

    private String name;
    private String club;
    private String email;
    private String userid;
    private String group;

    public UserClass() {
    }

    public UserClass(String name, String club, String email, String group) {
        this.name = name;
        this.club = club;
        this.email = email;
        this.group = group;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}