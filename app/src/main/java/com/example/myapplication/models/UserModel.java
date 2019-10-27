package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_s_name")
    private String user_s_name;

    @SerializedName("user_tel")
    private String user_tel;

    @SerializedName("user_username")
    private String user_username;

    @SerializedName("user_password")
    private String user_password;

    @SerializedName("user_line_id")
    private String user_line_id;

    @SerializedName("money_coin")
    private String money_coin;

    @SerializedName("status")
    private int status;

    @SerializedName("description")
    private String description;

    public UserModel(
            String user_id,
            String user_name,
            String user_s_name,
            String user_tel,
            String user_username,
            String user_password,
            String user_line_id,
            String money_coin,
            int status,
            String description
    ){
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_s_name = user_s_name;
        this.user_tel = user_tel;
        this.user_username = user_username;
        this.user_password = user_password;
        this.user_line_id =user_line_id;
        this.money_coin = money_coin;
        this.status = status;
        this.description = description;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_s_name(String user_s_name) {
        this.user_s_name = user_s_name;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setUser_line_id(String user_line_id) {
        this.user_line_id = user_line_id;
    }

    public void setMoney_coin(String money_coin) {
        this.money_coin = money_coin;
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

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_s_name() {
        return user_s_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_line_id() {
        return user_line_id;
    }

    public String getMoney_coin() {
        return money_coin;
    }

}
