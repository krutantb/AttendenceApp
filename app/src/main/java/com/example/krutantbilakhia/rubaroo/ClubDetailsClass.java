package com.example.krutantbilakhia.rubaroo;

public class ClubDetailsClass {

    public String clubName;
    public int clubCount;

    ClubDetailsClass(){
    }

    ClubDetailsClass(String clubName, int clubCount)
    {
        this.clubName = clubName;
        this.clubCount = clubCount;
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
}
