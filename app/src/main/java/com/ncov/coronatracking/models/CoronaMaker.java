package com.ncov.coronatracking.models;

public class CoronaMaker {
    private String province;
    private String country;
    private double lat;
    private double lon;
    private int confirm;
    private int death;
    private int recover;

    public CoronaMaker(String province, String country, double lat, double lon, int confirm, int death, int recover) {
        this.province = province;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
        this.confirm = confirm;
        this.death = death;
        this.recover = recover;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getRecover() {
        return recover;
    }

    public void setRecover(int recover) {
        this.recover = recover;
    }
}
