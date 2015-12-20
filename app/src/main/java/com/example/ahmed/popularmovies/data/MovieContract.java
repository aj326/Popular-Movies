package com.example.ahmed.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ahmed on 12/9/15.
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY =
            "com.example.ahmed.popularmovies.data.MovieProvider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Column with the foreign key into the review table and to fetch reviews and trailers.
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_POPULARITY = "popularity";

        public static final String COLUMN_RELEASE_DATE = "release_date";


        public static final String COLUMN_SORT_BY_RATING = "sort_by_rating" ;

        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        // .../movie/movie_id
        public static Uri buildMovieUri(long movie_id) {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static Uri buildMovieOnlyFavUri() {
            return CONTENT_URI.buildUpon().
                    appendQueryParameter(COLUMN_IS_FAVORITE, "1").build();
        }
        // .../movie/movie_id/review
        public static Uri buildMovieIdWithReview(long movie_id) {
            return buildMovieUri(movie_id).buildUpon().appendPath(PATH_REVIEW).build();
        }
        // .../movie/movie_id/trailer
        public static Uri buildMovieIdWithTrailer(long movie_id) {
            return buildMovieUri(movie_id).buildUpon().appendPath(PATH_TRAILER).build();
        }
    }
    /* Inner class that defines the table contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_URL = "url";

//        public static Uri buildTrailerUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
    }

    /* Inner class that defines the table contents of the reviews table */
    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Table name
        public static final String TABLE_NAME = "review";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CONTENT = "content";

//        public static Uri buildReviewUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

    }

}
