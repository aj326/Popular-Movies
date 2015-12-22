package com.example.ahmed.popularmovies.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ahmed on 12/9/15.
 */
public class MovieProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    //For debugging purposes
    public static final int MOVIE = 100;
    public static final int REVIEW = 200;
    public static final int TRAILER = 300;

    //.../movie/movie_id
    public static final int MOVIE_WITH_ID = 101;

    // .../movie/movie_id/review
    public static final int MOVIE_WITH_REVIEW = 102;

    // .../movie/movie_id/trailer
    public static final int MOVIE_WITH_TRAILER = 103;


    private static final SQLiteQueryBuilder sJoinedMoviesReviewsQueryBuilder;
    private static final SQLiteQueryBuilder sJoinedMoviesTrailersQueryBuilder;

    static {
        sJoinedMoviesReviewsQueryBuilder = new SQLiteQueryBuilder();
        sJoinedMoviesReviewsQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + " JOIN " +
                MovieContract.ReviewEntry.TABLE_NAME + " ON " +
                MovieContract.ReviewEntry.TABLE_NAME + "." +
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = " +
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " "
//                +
//                  " JOIN " +
//                MovieContract.TrailerEntry.TABLE_NAME + " ON " +
//                MovieContract.MovieEntry.TABLE_NAME + "." +
//                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " +
//                MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID
        );
    }

    static {
        sJoinedMoviesTrailersQueryBuilder = new SQLiteQueryBuilder();
        sJoinedMoviesTrailersQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + " JOIN " +
                MovieContract.TrailerEntry.TABLE_NAME + " ON " +
                MovieContract.MovieEntry.TABLE_NAME + "." +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " +
                MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " "
//                +
//                  " JOIN " +
//                MovieContract.TrailerEntry.TABLE_NAME + " ON " +
//                MovieContract.MovieEntry.TABLE_NAME + "." +
//                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " +
//                MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID
        );
    }

    //    private Cursor getMovieByID(Uri uri, String[] projection, String sortOrder) {
//        String review = MovieContract.MovieEntry.getMovieIDFromUri(uri);
//        String[] selectionArgs;
//        String selection = MovieContract.MovieEntry.TABLE_NAME+
//                           "."+ MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
//
//
//
//        return sMovieWithReviewsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                                                           projection,
//                                                           selection,
//                                                           null,
//                                                           null,
//                                                           null,
//                                                           sortOrder
//        );
//    }
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        //For debugging purposes
        matcher.addURI(authority, MovieContract.PATH_MOVIE,
                       MOVIE); //com.example.ahmed.popularmovies.data.MovieProvider/movie
        matcher.addURI(authority, MovieContract.PATH_REVIEW,
                       REVIEW); //com.example.ahmed.popularmovies.data.MovieProvider/review
        matcher.addURI(authority, MovieContract.PATH_TRAILER,
                       TRAILER); //com.example.ahmed.popularmovies.data.MovieProvider/trailer


        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/",
                       MOVIE_WITH_ID); //com.example.ahmed.popularmovies.data.MovieProvider/movie/#

        //com.example.ahmed.popularmovies.data.MovieProvider/movie/#/review
        matcher.addURI(authority,
                       MovieContract.PATH_MOVIE + "/#/" + MovieContract.PATH_REVIEW,
                       MOVIE_WITH_REVIEW);

        //com.example.ahmed.popularmovies.data.MovieProvider/movie/#/trailer
        matcher.addURI(authority,
                       MovieContract.PATH_MOVIE + "/#/" + MovieContract.PATH_TRAILER,
                       MOVIE_WITH_TRAILER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    private static final String mSelection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID
                                             + " = ?";

    @Nullable
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                retCursor = getMovieWithId(uri, projection, sortOrder);
                break;
            }
            case MOVIE_WITH_TRAILER: {
                retCursor = sJoinedMoviesTrailersQueryBuilder.query(
                        mOpenHelper.getReadableDatabase(), projection, mSelection, new String[]{
                                uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            }
            case MOVIE_WITH_REVIEW: {
                retCursor = sJoinedMoviesReviewsQueryBuilder.query(
                        mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }

            default:
                throw new NullPointerException(
                        "Failed to query: " + uri.toString() + " " + selection);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMovieWithId(Uri uri, String[] projection, String sortOrder) {
        String id = MovieContract.MovieEntry.getMovieIdFromUri(uri);
        String[] selectionArgs = new String[]{id};
        return mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                                                       projection, mSelection, selectionArgs, null,
                                                       null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_REVIEW:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_TRAILER:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    //// TODO: 12/22/15 store movie_id in variable 
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE:
            case MOVIE_WITH_ID: {
                long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null,
                                                   values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(values.getAsLong(
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW:
            case MOVIE_WITH_REVIEW: {
                long _id = db.insertWithOnConflict(MovieContract.ReviewEntry.TABLE_NAME, null,
                                                   values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieIdWithReview(values.getAsLong(
                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TRAILER:
            case MOVIE_WITH_TRAILER: {
                long _id = db.insertWithOnConflict(MovieContract.TrailerEntry.TABLE_NAME, null,
                                                   values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieIdWithTrailer(values.getAsLong(
                            MovieContract.TrailerEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case MOVIE: {
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILER: {
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
            case MOVIE_WITH_ID: {
                rowsUpdated = db.updateWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, values,
                                                      selection,
                                                      selectionArgs,
                                                      SQLiteDatabase.CONFLICT_REPLACE);
                break;
            }
            case REVIEW:
            case MOVIE_WITH_REVIEW: {
                rowsUpdated = db.updateWithOnConflict(MovieContract.ReviewEntry.TABLE_NAME, values,
                                                      selection,
                                                      selectionArgs,
                                                      SQLiteDatabase.CONFLICT_REPLACE);
                break;
            }
            case TRAILER:
            case MOVIE_WITH_TRAILER: {
                rowsUpdated = db.updateWithOnConflict(MovieContract.TrailerEntry.TABLE_NAME, values,
                                                      selection,
                                                      selectionArgs,
                                                      SQLiteDatabase.CONFLICT_REPLACE);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    //// TODO: 12/22/15 clean up code! make a local variable hold the table name.
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;

        switch (match) {
            case MOVIE:
            case MOVIE_WITH_ID:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME,
                                                           null, value,
                                                           SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case REVIEW:
            case MOVIE_WITH_REVIEW:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.ReviewEntry.TABLE_NAME,
                                                           null, value,
                                                           SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILER:
            case MOVIE_WITH_TRAILER:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.TrailerEntry.TABLE_NAME,
                                                           null, value,
                                                           SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = MovieContract.BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    //For testing to run smoothly
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
