package com.ncov.coronatracking.models;

public class CoronaMain {
    private int confirm;
    private int death;
    private int recover;
    private int country;
    private float percentageDeath;
    private float percentageRecover;

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public float getPercentageDeath() {
        return percentageDeath;
    }

    public void setPercentageDeath(float percentageDeath) {
        this.percentageDeath = percentageDeath;
    }

    public float getPercentageRecover() {
        return percentageRecover;
    }

    public void setPercentageRecover(float percentageRecover) {
        this.percentageRecover = percentageRecover;
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
