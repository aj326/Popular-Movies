package com.example.ahmed.popularmovies.utils;

import com.example.ahmed.popularmovies.BuildConfig;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ahmed on 12/22/15.
 */
public interface Constants {
    String API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;
    String BASE_URL = "http://api.themoviedb.org";
    String ARG_SORTING = "sorting";


    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build();
    String DETAIL_URI = "URI";
    int CURSOR_LOADER_ID = 0;
    int DETAIL_LOADER = 1;
    int REVIEW_LOADER = 2;
    int TRAILER_LOADER = 3;
    String DETAILFRAGMENT_TAG = "DFTAG";
    String HASHTAG_PROJECT = "#PopularMovies";
    String MOVIE_NAME = "MOVIE_NAME";

    enum DETAIL_COLUMNS {
        _ID,
        MOVIE_ID,
        TITLE,
        POSTER,
        PLOT,
        RELEASE_DATE,
        POPULARITY,
        RATING,
        SORT_BY_RATING,
        IS_FAVORITE
    }

    enum TRAILER_COLUMNS {
        _ID,
        NAME,
        URL
    }

    enum REVIEW_COLUMNS {
        _ID,
        AUTHOR,
        CONTENT
    }
}
