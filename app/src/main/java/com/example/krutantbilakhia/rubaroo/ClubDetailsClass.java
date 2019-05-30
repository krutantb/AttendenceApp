package com.example.krutantbilakhia.rubaroo;

public class ClubDetailsClass {

    public String clubName;
    public int clubCount;
    private double percentage;

    ClubDetailsClass(){
    }

    ClubDetailsClass(String clubName, int clubCount, double percentage)
    {
        this.clubName = clubName;
        this.clubCount = clubCount;
        this.percentage = percentage;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getClubCount() {
        return clubCount;
    }

    public void setClubCount(int clubCount) {
        this.clubCount = clubCount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
