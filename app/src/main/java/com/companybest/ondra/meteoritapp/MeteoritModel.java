package com.companybest.ondra.meteoritapp;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MeteoritModel extends RealmObject {

    @PrimaryKey
    private int id;

    private float mass;
    private int year;
    private String name;

    private double lat;
    private double lng;


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
