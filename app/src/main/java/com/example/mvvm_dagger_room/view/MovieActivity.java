package com.example.mvvm_dagger_room.view;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


import com.example.mvvm_dagger_room.R;
import com.example.mvvm_dagger_room.adapter.MovieAdapter;
import com.example.mvvm_dagger_room.app.MyApplication;
import com.example.mvvm_dagger_room.databinding.ActivityMainBinding;
import com.example.mvvm_dagger_room.model.Movie;
import com.example.mvvm_dagger_room.model.ResponseMovie;
import com.example.mvvm_dagger_room.model_db.Movie_DB;
import com.example.mvvm_dagger_room.room_db.DatabaseClient;
import com.example.mvvm_dagger_room.viewmodel.MovieViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class MovieActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;
    @Inject
    Application application;
    private ActivityMainBinding binding;
    private MovieViewModel movieViewModel;
    public static ProgressDialog progressDialog;
    List<Movie> movies = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ((MyApplication) getApplication()).getComponent().inject(MovieActivity.this);
        binding.setLifecycleOwner(this);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        showLoadingBar();

        observableChanges();

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetFilter();
            }
        });
    }

    private void observableChanges() {
        movieViewModel.movieList.observe(this, responseMovie -> populateRecyclerView(responseMovie));

        movieViewModel.errorMessage.observe(this, s -> showErrorMessage(s));

//        movieViewModel.showLoadingProg.observe(this, show -> {
//            if (show)
//                showLoadingBar();
//            else
//                hideLoadingBar();
//        });
    }

    private void showErrorMessage(String s) {
        Toast.makeText(MovieActivity.this, s, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();

        if(s.contentEquals(getString(R.string.no_internet_connection))){
            /**
             *  Insert and get data using Database Async way
             */
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int size= DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAllMovieItems().size();

                    List<Movie_DB> db_movies = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAllMovieItems();
                    List<Movie> movieDB = new ArrayList<>();

                    for(int i =0 ; i <size;i++) {
                        //adding to database
                        Movie_DB movieObj = db_movies.get(i);
                        Movie obj = new Movie(movieObj.getId(), movieObj.getVoteCount(), movieObj.isVideo(), movieObj.getVoteAverage(),
                                movieObj.getTitle(), movieObj.getPopularity(),movieObj.posterPath,movieObj.getOriginalLanguage(),movieObj.getOriginalTitle(),
                                movieObj.getBackdropPath(),movieObj.isAdult(),movieObj.getOverview(),movieObj.getReleaseDate());

                        movieDB.add(obj);
                    }

                    Recycler(movieDB);
                }
            });

        }
    }

    private void populateRecyclerView(ResponseMovie responseMovie) {
        movies = responseMovie.getMovies();
        Recycler(movies);
        /**
         *  Insert and get data using Database Async way
         */
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for(int i =0 ; i <movies.size();i++) {
                    //adding to database
                    Movie movieObj = movies.get(i);
                    Movie_DB obj = new Movie_DB(movieObj.getId(), movieObj.getVoteCount(), movieObj.isVideo(), movieObj.getVoteAverage(),
                            movieObj.getTitle(), movieObj.getPopularity(),movieObj.posterPath,movieObj.getOriginalLanguage(),movieObj.getOriginalTitle(),
                            movieObj.getBackdropPath(),movieObj.isAdult(),movieObj.getOverview(),movieObj.getReleaseDate());
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .addMovieItems(obj);
                }

            }
        });
            }


    private void Recycler(List<Movie> movies) {
        binding.setAdapter(new MovieAdapter(movies, MovieActivity.this));
    }


    private void openBottomSheetFilter(){
        final LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_filter, null);
        TextView rating = btnsheet.findViewById(R.id.tvRating);
        TextView date = btnsheet.findViewById(R.id.tvDate);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(btnsheet);

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Collections.sort(movies, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie lhs, Movie rhs) {
                        Double first = lhs.getVoteAverage()/2;
                        Double second = rhs.getVoteAverage()/2;
                        return first.compareTo(second);
                    }
                });
                Log.e("rating sort" , movies.toString());
                Recycler(movies);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //your own date format

                Collections.sort(movies, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie lhs, Movie rhs) {
                        String first = lhs.getReleaseDate();
                        String second = rhs.getReleaseDate();
                        try {
                            return simpleDateFormat.parse(first).compareTo(simpleDateFormat.parse(second));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
                Recycler(movies);
            }
        });
        dialog.show();
    }
}