package com.example.ahmed.popularmovies.rest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ahmed on 10/7/15.
 */
public interface TMDBFetchService {
    String MY_API_KEY = "0d2f78cd5f086e8d35e0274952749495";

    @GET("/3/discover/movie?api_key=" + TMDBFetchService.MY_API_KEY)
    Call<MoviesFromTMDB> movieList(@Query("page") Integer page);

}
