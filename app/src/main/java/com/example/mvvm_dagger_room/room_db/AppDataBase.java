package com.example.mvvm_dagger_room.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mvvm_dagger_room.model_db.Movie_DB;


@Database(entities = {Movie_DB.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static RoomDatabase instance;
    public abstract DAO taskDao();

    public AppDataBase() {
    }

    public static RoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            "product_database")
                            .build();

                }
            }
        }
        return instance;
    }

}
