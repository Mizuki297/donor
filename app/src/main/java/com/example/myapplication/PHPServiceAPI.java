package com.example.myapplication;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

//เรียกการส่ง api
public interface PHPServiceAPI {

    @GET("/api/get_post_hpt.php")
    Call<List<Hospital>> getHospital();

    @GET("/api/get_post_user.php")
    Call<List<User>> getUser();

    //บัคการส่งข้อมูล
    @GET("/api/select_data.php")
    Call<List<NumCat>> numCat(
            @Part("user_id") int user_id,
            @Part("HPT_name") String HPT_name,
            @Part("Blood_type") String Blood_type

    );

    @POST("/api/get_post_request.php")
    Call<PostData> createPost(@Body PostData post);

    // เชื่อมต่อกับ PHPService โดยส่งข้อมูลแบบ From-data
    // /api/get_post_request.php?HPT_name="ชื่อโรงพยาบาล*"&Blood_type="กรุ๊ปเลือ"&user_id=""
    @FormUrlEncoded
    @POST("/api/get_post_request.php")
    Call<Void> createPost(
            @Field("HPT_name")String HPT_name,

            @Field("Blood_type")String Blood_type,

            @Field("user_id")int user_id
    );
}
