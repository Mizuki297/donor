package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class CatModel {
    @SerializedName("cat_id")
    private String cat_id;

    @SerializedName(("cat_name"))
    private String cat_name;

    @SerializedName("url_cat")
    private String url_cat;

    @SerializedName("blood_type")
    private String blood_type;

    @SerializedName("cat_type")
    private String cat_type;

    @SerializedName("cat_weight")
    private String cat_weight;

    @SerializedName("cat_bd")
    private String cat_bd;

    @SerializedName("health_check_date")
    private String health_check_date;

    @SerializedName("latest_donation")
    private String latest_donation;

    @SerializedName("status_cat")
    private String status_cat;

    @SerializedName("user_line_id")
    private String user_line_id;

    @SerializedName("user_tel")
    private String user_tel;


    public CatModel(
            String cat_id,
            String cat_name,
            String url_cat,
            String blood_type,
            String cat_type,
            String cat_weight,
            String cat_bd,
            String health_check_date,
            String latest_donation,
            String status_cat,
            String user_line_id,
            String user_tel
    ) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.url_cat = url_cat;
        this.blood_type = blood_type;
        this.cat_type = cat_type;
        this.cat_weight = cat_weight;
        this.cat_bd = cat_bd;
        this.health_check_date = health_check_date;
        this.latest_donation = latest_donation;
        this.status_cat = status_cat;
        this.user_line_id = user_line_id;
        this.user_tel = user_tel;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getUrl_cat() {
        return url_cat;
    }

    public void setUrl_cat(String url_cat) {
        this.url_cat = url_cat;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getCat_weight() {
        return cat_weight;
    }

    public void setCat_weight(String cat_weight) {
        this.cat_weight = cat_weight;
    }

    public String getCat_bd() {
        return cat_bd;
    }

    public void setCat_bd(String cat_bd) {
        this.cat_bd = cat_bd;
    }

    public String getHealth_check_date() {
        return health_check_date;
    }

    public void setHealth_check_date(String health_check_date) {
        this.health_check_date = health_check_date;
    }

    public String getLatest_donation() {
        return latest_donation;
    }

    public void setLatest_donation(String latest_donation) {
        this.latest_donation = latest_donation;
    }

    public String getStatus_cat() {
        return status_cat;
    }

    public void setStatus_cat(String status_cat) {
        this.status_cat = status_cat;
    }

    public String getUser_line_id() {
        return user_line_id;
    }

    public void setUser_line_id(String user_line_id) {
        this.user_line_id = user_line_id;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
}
