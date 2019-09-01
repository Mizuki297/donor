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
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface PHPServiceAPI {

    @POST("get_post_request.php")
    Call<Request> createPost(@Body Request post);

    @FormUrlEncoded
    @POST("get_post_request.php")
    Call<Request> createPost(
            @Field("user_id")int user_id,
            @Field("Blood_type")String Blood_type,
            @Field("HPT_name")String HPT_name,
            @Field("body")String text
    );

    @FormUrlEncoded
    @POST("get_post_request.php")
    Call<Request> createPost(@FieldMap Map<String, String> fields);
}
