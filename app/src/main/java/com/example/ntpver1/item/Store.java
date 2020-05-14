package com.example.ntpver1.item;

import java.util.ArrayList;

public class Store {
    ArrayList<String> pays;
    String name;
    String phone;
    String type;
    double latitude;
    double longitude;
    int star;

    public Store(ArrayList<String> pays, String name, String phone, String type, int star, double latitude, double longitude) {
        this.pays = pays;
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.star = star;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ArrayList<String> getPays() {
        return pays;
    }

    public void setPays(ArrayList<String> pays) {
        this.pays = pays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
