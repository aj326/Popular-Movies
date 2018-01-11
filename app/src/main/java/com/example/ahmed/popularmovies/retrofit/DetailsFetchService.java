package com.example.ahmed.popularmovies.retrofit;

import com.example.ahmed.popularmovies.pojo.MoviesFromTMDB;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

import static com.example.ahmed.popularmovies.utils.Constants.API_KEY;

/**
 * Created by ahmed on 10/7/15.
 */
public interface DetailsFetchService {
    @GET("/3/discover/movie?api_key=" + API_KEY)
    Call<MoviesFromTMDB> movieList(@Query("page") Integer page);
}
