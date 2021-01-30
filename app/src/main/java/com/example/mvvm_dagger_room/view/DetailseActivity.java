package com.example.mvvm_dagger_room.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.mvvm_dagger_room.R;
import com.example.mvvm_dagger_room.databinding.ActivityDetailseBinding;
import com.example.mvvm_dagger_room.model.Movie;


public class DetailseActivity extends AppCompatActivity {
    private ActivityDetailseBinding detailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_detailse);
        Intent intent = getIntent();
        if (intent != null) {
            Movie movie = intent.getParcelableExtra("Movie");
            detailsBinding.setMovie(movie);
        }
    }
}
