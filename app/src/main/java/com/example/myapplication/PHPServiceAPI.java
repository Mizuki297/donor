package com.example.myapplication;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PHPServiceAPI {

    @POST("get_post_request.php")
    Call<PostData> createPost(@Body PostData post);

    @FormUrlEncoded
    @POST("get_post_request.php")
    Call<PostData> createPost(
            @Field("user_id")int user_id,
            @Field("Blood_type")String Blood_type,
            @Field("HPT_name")String HPT_name,
            @Field("body")String text
    );

    @FormUrlEncoded
    @POST("get_post_request.php")
    Call<PostData> createPost(@FieldMap Map<String, String> fields);
}
