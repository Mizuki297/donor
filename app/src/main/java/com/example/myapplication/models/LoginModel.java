package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("user_id")
    public String user_id ;
    @SerializedName("status")
    public int status;
    @SerializedName("description")
    public String description;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
