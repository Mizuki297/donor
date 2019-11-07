package com.example.myapplication.services;

import com.example.myapplication.models.CatModel;
import com.example.myapplication.models.HospitalModel;
import com.example.myapplication.models.LoginModel;
import com.example.myapplication.models.NumCatModel;
import com.example.myapplication.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//เรียกการส่ง api
public interface PHPServiceAPI {

    @POST("/api/donor_startus.php")
    Call<Void> update_data();

    @GET("/api/get_post_hpt.php")
    Call<List<HospitalModel>> getHospital();

    @FormUrlEncoded
    @POST("/api/get_post_user.php")
    Call<UserModel> getUser(
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("/api/update_coin.php")
    Call<Void> updateCoin(
            @Field("user_id")String user_id
    );
    //บัคการส่งข้อมูล
    @FormUrlEncoded
    @POST("/api/select_data.php")
    Call<NumCatModel> numCat(
            @Field("user_id") String user_id,
            @Field("HPT_name") String HPT_name,
            @Field("Blood_type") String Blood_type

    );

    @FormUrlEncoded
    @POST("api/select_Cat.php")
    Call<List<CatModel>> getcat_list(
            @Field("HPT_name") String HPT_name,
            @Field("blood_type") String blood_type
    );

    @FormUrlEncoded
    @POST("api/show_data_cat.php")
    Call<CatModel> getCatDetail(
            @Field("cat_id") String cat_id
    );
    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginModel> login(
            @Field("username")String username,
            @Field("password")String password
    );

    @FormUrlEncoded
    @POST("api/register.php")
    Call<UserModel> numUser(
            @Field("user_name")String user_name,
            @Field("user_s_name")String user_s_name,
            @Field("username")String username,
            @Field("password")String password,
            @Field("user_email")String user_email,
            @Field("user_tel")String user_tel,
            @Field("user_line_id")String user_line_id,
            @Field("HPT_name")String HPT_name
    );
    @FormUrlEncoded
    @POST("/api/add_cat2.php")
    Call<Void> AddCat(

            @Field("cat_name") String cat_name,
            @Field("cat_type") String cat_type,
            @Field("blood_type") String blood_type,
            @Field("cat_bd") String cat_bd,
            @Field("cat_weight") String cat_weight,
            @Field("health_check_date") String health_check_date,
            @Field("latest_donation") String latest_donation,
//            @Field("HPT_id") String HTP_id,
            @Field("user_id") String user_id,
            @Field("file") String Url_image

    );
    @FormUrlEncoded
    @POST("api/show_CatRegister.php")
    Call<List<CatModel>> getCatRegister(
            @Field("user_id")String user_id
    );
    @FormUrlEncoded
    @POST("/api/update_cat.php")
    Call<Void> UpdateCat(
            @Field("cat_id")String cat_id,
            @Field("cat_name") String cat_name,
            @Field("cat_type") String cat_type,
            @Field("blood_type") String blood_type,
            @Field("cat_bd") String cat_bd,
            @Field("cat_weight") String cat_weight,
            @Field("health_check_date") String health_check_date,
            @Field("latest_donation") String latest_donation
    );
    @FormUrlEncoded
    @POST("/api/update_status.php")
    Call<Void>Update_Status(
            @Field("cat_id") String cat_id,
            @Field("status_cat")String status_cat
    );
    @FormUrlEncoded
    @POST("api/update_user.php")
    Call<Void> update_user(
            @Field("user_id")String user_id,
            @Field("user_name")String user_name,
            @Field("user_s_name")String user_s_name,
            @Field("user_tel")String user_tal,
            @Field("user_line_id")String user_line_id
    );
    @FormUrlEncoded
    @POST("api/update_pass.php")
    Call<LoginModel> update_pass(
            @Field("user_id")String user_id,
            @Field("old_pass")String old_pass,
            @Field("password")String password
    );
    @FormUrlEncoded
    @POST("api/add_coin.php")
    Call<UserModel> add_coin(
            @Field("user_id")String user_id,
            @Field("coin")int coin
    );


}
