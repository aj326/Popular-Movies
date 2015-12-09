package com.example.ahmed.popularmovies.rest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by ahmed on 10/7/15.
 */
public interface TrailersUrlFetchService {
    @GET("/3/movie/{id}/videos?api_key=" + API_KEY.API_KEY)
    Call<MovieVideos> trailerList(@Path("id") Integer id);
}
