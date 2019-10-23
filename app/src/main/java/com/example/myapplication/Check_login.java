package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Check_login {

    @SerializedName("user_id")
    public String user_id;

    public Check_login(
            String user_id
    ) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
