package com.example.ahmed.popularmovies.retrofit;

import com.example.ahmed.popularmovies.pojo.MovieVideos;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

import static com.example.ahmed.popularmovies.utils.Constants.API_KEY;

/**
 * Created by ahmed on 10/7/15.
 */
public interface TrailersFetchService {
    @GET("/3/movie/{id}/videos?api_key=" + API_KEY)
    Call<MovieVideos> trailerList(@Path("id") long id);
}
