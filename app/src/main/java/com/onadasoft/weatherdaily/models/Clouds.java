package com.onadasoft.weatherdaily.models;

public class Clouds {
    private long all;

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                "}";
    }
}
