package com.example.ahmed.popularmovies.rest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by ahmed on 10/7/15.
 */
public interface MovieReviewsFetchService {

    @GET("/3/movie/{id}/reviews?api_key=" + API_KEY.API_KEY)
    Call<MovieReviews> reviewsList(@Path("id") long id);

}
