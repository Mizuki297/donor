package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class NumCatModel {

    @SerializedName("numCat")
    private String numCat;

    public void setNumCat(String numCat) {
        this.numCat = numCat;
    }

    public String getNumCat() {
        return numCat;
    }

}
