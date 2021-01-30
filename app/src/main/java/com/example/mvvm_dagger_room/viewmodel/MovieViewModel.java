package com.example.mvvm_dagger_room.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.mvvm_dagger_room.R;
import com.example.mvvm_dagger_room.model.ResponseMovie;
import com.example.mvvm_dagger_room.repository.MovieRepository;
import com.example.mvvm_dagger_room.util.Connection;
import com.example.mvvm_dagger_room.util.Constant;

import java.util.HashMap;
import java.util.Map;

public class MovieViewModel extends AndroidViewModel {
    public MutableLiveData<ResponseMovie> movieList = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MovieRepository movieRepository = new MovieRepository();
    private boolean isConnected;

    public MovieViewModel(@NonNull Application application) {
        super(application);
    }

    public void getMovies(Context context) {

        isConnected = Connection.isNetworkAvailable(getApplication());
        if (isConnected) {
            Map<String, String> map = new HashMap<>();
            map.put(Constant.Api.TOKEN_NAME, Constant.Api.TOKEN_VALUE);
            movieList = movieRepository.getMovies(map , context);

        } else {
            errorMessage.setValue(context.getString(R.string.no_internet_connection));

        }
    }
}