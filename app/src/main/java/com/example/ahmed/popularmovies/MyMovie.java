package com.example.ahmed.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by ahmed on 9/28/15.
 *
 * original title
 *movie poster image thumbnail
 *A plot synopsis (called overview in the api)
 *user rating (called vote_average in the api)
 *release date
 */
public class MyMovie {
    private final String imgUrl;
    private final String title;
    private final String plot;
    private final String rating;
    private final String date;


    public String getTitle() {
        return this.title;
    }

    public String getPlot() {
        return this.plot;
    }

    public String getRating() {
        return this.rating;
    }

    public String getDate() {
        return this.date;
    }

    private ImageView imageView;

    public String getImgUrl() {
        return this.imgUrl;
    }

    public MyMovie(String title, String img_url, String plot, String rating, String date){
        imgUrl = Uri.parse("http://image.tmdb.org/t/p/w500/").buildUpon()
                .appendEncodedPath(img_url)
                .build().toString();
        this.title = title;
        this.date = date;
        this.plot = plot;
        this.rating = rating + "/10";
    }
    public Bundle bundleMovie(){
        Bundle bundle = new Bundle();
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("title", title);
        bundle.putString("plot", plot);
        bundle.putString("rating", rating);
        bundle.putString("date", date);
        return bundle;
    }

}
