package com.example.myapplication;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PHPServiceAPI {

    @GET("/api/get_post_hpt.php")
    Call<List<Hospital>> getHospital();

    @POST("/api/get_post_request.php")
    Call<PostData> createPost(@Body PostData post);

    @FormUrlEncoded
    @POST("/api/get_post_request.php")
    Call<PostData> createPost(
            @Field("HPT_name")String HPT_name,

            @Field("Blood_type")String Blood_type,

            @Field("user_id")int user_id
    );

    @FormUrlEncoded
    @POST("/api/get_post_request.php")
    Call<PostData> createPost(@FieldMap Map<String, String> fields);
}
