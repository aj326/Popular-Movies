package com.example.ahmed.popularmovies.retrofit;

import com.example.ahmed.popularmovies.pojo.MovieVideos;
import com.example.ahmed.popularmovies.utils.Constants;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by ahmed on 10/7/15.
 */
public interface TrailersFetchService {
    @GET("/3/movie/{id}/videos?api_key=" + Constants.API_KEY)
    Call<MovieVideos> trailerList(@Path("id") long id);
}
