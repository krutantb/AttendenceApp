package com.example.krutantbilakhia.rubaroo;

public class ClubDetailsClass {

    public String clubName;
    public int clubTotal;

    ClubDetailsClass(){
    }

    ClubDetailsClass(String clubName, int clubTotal)
    {
        this.clubName = clubName;
        this.clubTotal = clubTotal;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getClubTotal() {
        return clubTotal;
    }

    public void setClubTotal(int clubTotal) {
        this.clubTotal = clubTotal;
    }
}
