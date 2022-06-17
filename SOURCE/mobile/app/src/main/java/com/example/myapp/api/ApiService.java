package com.example.myapp.api;

import com.example.myapp.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    //http://20.205.122.62:3000/
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://20.205.122.62:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("login")
    Call<LoginResult> sendPosts(@Body LoginResult loginResult);

}
