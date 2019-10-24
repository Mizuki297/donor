package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Check_login {

    @SerializedName("user_id")
    public String user_id;
    @SerializedName("token")
    public String token;

    public Check_login(
            String user_id,
            String token
    ) {
        this.user_id = user_id;
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }
}
