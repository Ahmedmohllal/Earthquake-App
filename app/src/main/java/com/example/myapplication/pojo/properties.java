package com.example.myapplication.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "properties_table")
public class properties {

    @PrimaryKey(autoGenerate = true)
    int id;
    double mag;
    String place;
    String url;

    public properties(double mag, String place, String url) {
        this.mag = mag;
        this.place = place;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public String getUrl() {
        return url;
    }
}
