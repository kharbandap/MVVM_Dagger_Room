package com.example.mvvm_dagger_room.api;
import com.example.mvvm_dagger_room.model.ResponseMovie;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("discover/movie")
    Call<ResponseMovie> getMovies(@QueryMap Map<String, String> queryParameters);
}
