package com.mylibrary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostEndpoint {
    @GET("posts")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post> savePost(@Body Post post);
}
