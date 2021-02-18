package com.ncov.coronatracking.models;

public class CoronaCountry {
    private int confirm;
    private int death;
    private int recover;
    private int province;
    private float percentageDeath;
    private float percentageRecover;

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

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
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
}
