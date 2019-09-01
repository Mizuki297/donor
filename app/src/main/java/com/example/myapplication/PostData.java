package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class PostData {

    private int user_id;
    private String blood_type, HPT_name;

    @SerializedName("body")
    private String text;

    public PostData(int user_id, String blood_type, String HPT_name, String text) {
        this.user_id = user_id;
        this.blood_type = blood_type;
        this.HPT_name = HPT_name;
        this.text = text;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public String getHPT_name() {
        return HPT_name;
    }

    public String getText() {
        return text;
    }
}

