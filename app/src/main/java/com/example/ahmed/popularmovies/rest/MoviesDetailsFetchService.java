package com.example.ahmed.popularmovies.rest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ahmed on 10/7/15.
 */
public interface MoviesDetailsFetchService {
    @GET("/3/discover/movie?api_key=" + API_KEY.API_KEY)
    Call<MoviesFromTMDB> movieList(@Query("page") Integer page);

}
