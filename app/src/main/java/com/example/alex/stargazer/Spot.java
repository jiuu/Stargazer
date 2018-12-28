package com.example.alex.stargazer;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


@Entity
public class Spot {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "longitude")
    private double longitude;

    @ColumnInfo (name = "latitude")
    private double latitude;

    @ColumnInfo (name = "info")
    private String info;

    public Spot(String name, double longitude, double latitude, String info) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {

        return name;
    }


    public String getInfo() {
        return info;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
