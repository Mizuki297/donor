package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class HospitalModel {

    @SerializedName("HPT_id")
    private String HPT_id;

    @SerializedName("HPT_name")
    private String HPT_name;

    @SerializedName("HPT_location")
    private String HPT_location;

    public void setHPT_id(String HPT_id) {
        this.HPT_id = HPT_id;
    }

    public void setHPT_name(String HPT_name) {
        this.HPT_name = HPT_name;
    }

    public void setHPT_location(String HPT_location) {
        this.HPT_location = HPT_location;
    }

    public String getHPT_id() {
        return HPT_id;
    }

    public String getHPT_name() {
        return HPT_name;
    }

    public String getHPT_location() {
        return HPT_location;
    }
}
