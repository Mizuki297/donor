package com.example.myapplication;

public class PostData {

    private int user_id;
    private String Blood_type, HPT_name;

    public PostData(String HPT_name, String Blood_type, int user_id) {
        this.user_id = user_id;
        this.Blood_type = Blood_type;
        this.HPT_name = HPT_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getBlood_type() {
        return Blood_type;
    }

    public String getHPT_name() {
        return HPT_name;
    }
}

