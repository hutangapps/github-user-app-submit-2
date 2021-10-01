package com.gnm.githubusersubmit2.api;

import com.gnm.githubusersubmit2.model.ModelFollower;
import com.gnm.githubusersubmit2.model.ModelSearch;
import com.gnm.githubusersubmit2.model.ModelUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/search/users")
    Call<ModelSearch> getSearch(@Query("q") String q, @Header("Authorization") String token);

    @GET("/users/{username}")
    Call<ModelUser> getUser(@Path("username") String username, @Header("Authorization") String token);

    @GET("/users/{username}/followers")
    Call<List<ModelFollower>> getFollower(@Path("username") String username, @Header("Authorization") String token);

    @GET("/users/{username}/following")
    Call<List<ModelFollower>> getFollowing(@Path("username") String username, @Header("Authorization") String token);

}