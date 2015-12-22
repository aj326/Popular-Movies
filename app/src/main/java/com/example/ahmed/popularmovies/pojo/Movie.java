
package com.example.ahmed.popularmovies.pojo;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Movie {



    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;
//    runtime": 115,
@SerializedName("runtime")
@Expose
private int runtime;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }




    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return The releaseDate
     */
    public int getRuntime() {
        return runtime;}
    public String getReleaseDate() {
        return releaseDate;
    }



    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }


    /**
     * @return The popularity
     */
    public Double getPopularity() {
        return popularity;
    }


    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }


    /**
     * @return The video
     */
//    public Boolean getVideo() {
//        return video;
//    }

    /**

    /**
     * @return The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }


    /**
     * @return The voteCount
     */
    public int getVoteCount() {
        return voteCount;
    }
    public double getTrueRating(){
        return voteCount*voteCount;
    }

    public String getImgUrl() {
        return Uri.parse("http://image.tmdb.org/t/p/w500/").buildUpon()
                .appendEncodedPath(posterPath)
                .build().toString();
    }

}
