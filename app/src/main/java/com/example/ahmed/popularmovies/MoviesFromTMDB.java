
package com.example.ahmed.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MoviesFromTMDB {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movie> results = new ArrayList<Movie>();
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Movie> getMovies() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setMovies(List<Movie> results) {
        this.results = results;
    }

    /**
     * @return The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return The totalMovies
     */
    public Integer getTotalMovies() {
        return totalMovies;
    }

    /**
     * @param totalMovies The total_results
     */
    public void setTotalMovies(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }

    public String toString() {
        return results.toString();
    }

}
