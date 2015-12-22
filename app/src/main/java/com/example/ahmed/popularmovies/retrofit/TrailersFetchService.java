package com.example.ahmed.popularmovies.retrofit;

import com.example.ahmed.popularmovies.pojo.MovieVideos;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import com.example.ahmed.popularmovies.utils.Constants;

/**
 * Created by ahmed on 10/7/15.
 */
public interface TrailersFetchService {
    @GET("/3/movie/{id}/videos?api_key=" + Constants.API_KEY)
    Call<MovieVideos> trailerList(@Path("id") long id);
}
