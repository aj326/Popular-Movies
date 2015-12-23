package com.example.ahmed.popularmovies.utils;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ahmed on 12/22/15.
 */
public interface Constants {
    String API_KEY = "0d2f78cd5f086e8d35e0274952749495";
    String BASE_URL = "http://api.themoviedb.org";


    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build();
    String DETAIL_URI = "URI";
    int CURSOR_LOADER_ID = 0;
    int DETAIL_LOADER = 1;
    int REVIEW_LOADER = 2;
    int TRAILER_LOADER = 3;
    String DETAILFRAGMENT_TAG = "DFTAG";

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
