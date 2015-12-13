package com.example.ahmed.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 12/9/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewsEntry.TABLE_NAME + " (" +
                                                 MovieContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY," +
                                                 MovieContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT, " +
                                                 MovieContract.ReviewsEntry.COLUMN_CONTENT + " TEXT " +
                                                 " );";

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                                                // Why AutoIncrement here, and not above?
                                                // Unique keys will be auto-generated in either case.  But for weather
                                                // forecasting, it's reasonable to assume the user will want information
                                                // for a certain date and all dates *following*, so the forecast data
                                                // should be sorted accordingly.
                                                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                                                // the ID of the location entry associated with this weather data
                                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT  NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_PLOT + " TEXT  NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +

                                              MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                                                MovieContract.MovieEntry.COLUMN_TRAILERS + " TEXT ," +
                                                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " TEXT NOT NULL, " +



                                                // Set up the location column as a foreign key to location table.
                                                " FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                                                MovieContract.ReviewsEntry.TABLE_NAME + " (" + MovieContract.ReviewsEntry._ID + "), " +

                                                // To assure the application have just one weather entry per day
                                                // per location, it's created a UNIQUE constraint with REPLACE strategy
                                                " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
