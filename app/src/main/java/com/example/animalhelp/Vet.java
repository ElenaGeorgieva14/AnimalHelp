package com.example.animalhelp;

public class Vet {
    private double latitude;
    private double longitude;
    private String name;
    private boolean isOpenNow;

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getName() { return name; }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getIsOpenNow() { return isOpenNow; }

    public void setOpenNow(boolean isOpenNow)
    {
        this.isOpenNow = isOpenNow;
    }

    public Vet(double latitude, double longitude, String name, boolean isOpenNow){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.isOpenNow = isOpenNow;
    }

    public Vet(double latitude, double longitude, String name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
