package com.example.mvvm_dagger_room.app;

import android.app.Application;

import com.example.mvvm_dagger_room.component.ApiComponent;
import com.example.mvvm_dagger_room.module.ApiClientModule;
import com.example.mvvm_dagger_room.module.AppModule;
import com.example.mvvm_dagger_room.util.Constant;
import com.example.mvvm_dagger_room.component.DaggerApiComponent;


public class MyApplication extends Application {

    public static ApiComponent mApiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiClientModule(new ApiClientModule(Constant.Api.BASE_URL))
                .build();
    }

    public static ApiComponent getComponent() {
        return mApiComponent;
    }
}