package com.example.mvvm_dagger_room.component;
import com.example.mvvm_dagger_room.module.ApiClientModule;
import com.example.mvvm_dagger_room.module.AppModule;
import com.example.mvvm_dagger_room.repository.MovieRepository;
import com.example.mvvm_dagger_room.view.MovieActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiClientModule.class, AppModule.class})
public interface ApiComponent {
    void inject(MovieActivity movieActivity);
    void injectMovie(MovieRepository movieRepository);
}