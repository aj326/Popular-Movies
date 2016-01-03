package com.example.ahmed.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.utils.Constants;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.DETAIL_URI, getIntent().getData());
            arguments.putString(Constants.MOVIE_NAME,
                                getIntent().getStringExtra(Constants.MOVIE_NAME));
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }


}
