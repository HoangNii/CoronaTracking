package com.ncov.coronatracking.models;

public class ChartData {
    private String date;
    private int confirm;
    private int deaths;
    private int recover;

    public ChartData(String date, int confirm, int deaths, int recover) {
        this.date = date;
        this.confirm = confirm;
        this.deaths = deaths;
        this.recover = recover;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecover() {
        return recover;
    }

    public void setRecover(int recover) {
        this.recover = recover;
    }
}
