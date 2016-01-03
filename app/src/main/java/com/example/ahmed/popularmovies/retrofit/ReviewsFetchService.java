package com.example.ahmed.popularmovies.retrofit;

import com.example.ahmed.popularmovies.pojo.MovieReviews;
import com.example.ahmed.popularmovies.utils.API_KEY;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by ahmed on 10/7/15.
 */
public interface ReviewsFetchService {

    @GET("/3/movie/{id}/reviews?api_key=" + API_KEY.API_KEY)
    Call<MovieReviews> reviewsList(@Path("id") long id);

}
