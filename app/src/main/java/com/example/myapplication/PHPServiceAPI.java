package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//เรียกการส่ง api
public interface PHPServiceAPI {

    @GET("/api/get_post_hpt.php")
    Call<List<Hospital>> getHospital();

    @FormUrlEncoded
    @POST("/api/get_post_user.php")
    Call<List<User>> getUser(
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
    Call<List<NumCat>> numCat(
            @Field("user_id") String user_id,
            @Field("HPT_name") String HPT_name,
            @Field("Blood_type") String Blood_type

    );

    @FormUrlEncoded
    @POST("/api/get_post_request.php")
    Call<Void> createPost(
            @Field("HPT_name")String HPT_name,

            @Field("Blood_type")String Blood_type,

            @Field("user_id")int user_id
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
    Call<Check_login> login(
            @Field("username")String username,
            @Field("password")String password
    );

    @FormUrlEncoded
    @POST("api/register.php")
    Call<List<User>> numUser(
            @Field("user_name")String user_name,
            @Field("user_s_name")String user_s_name,
            @Field("username")String username,
            @Field("password")String password,
            @Field("user_email")String user_email,
            @Field("user_tel")String user_tel,
            @Field("user_line_id")String user_line_id,
            @Field("HPT_name")String HPT_name
    );
}
