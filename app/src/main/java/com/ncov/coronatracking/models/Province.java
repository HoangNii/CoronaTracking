package com.ncov.coronatracking.models;

public class Province {
    private String name;
    private int confirm;
    private int death;
    private int recover;

    public Province(String name, int confirm, int death, int recover) {
        this.name = name;
        this.confirm = confirm;
        this.death = death;
        this.recover = recover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
