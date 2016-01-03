package com.example.ahmed.popularmovies.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ahmed.popularmovies.provider.MovieContract.*;

/**
 * Created by ahmed on 12/9/15.
 */
class MovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                                               ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                               ReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                                               ReviewEntry.COLUMN_AUTHOR + " TEXT UNIQUE, " +
                                               ReviewEntry.COLUMN_CONTENT + " TEXT UNIQUE, " +
                                               " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                                               MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "));";


        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                                                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                                                TrailerEntry.COLUMN_TRAILER_NAME + " TEXT UNIQUE, " +
                                                TrailerEntry.COLUMN_URL + " TEXT UNIQUE, " +
                                                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                                                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "));";


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                                              MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                              MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                                              MovieEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                                              MovieEntry.COLUMN_POSTER + " TEXT  NOT NULL, " +
                                              MovieEntry.COLUMN_PLOT + " TEXT  NOT NULL, " +
                                              MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                                              MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                                              MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                                              MovieEntry.COLUMN_SORT_BY_RATING + " REAL NOT NULL, " +
                                              MovieEntry.COLUMN_IS_FAVORITE + " INTEGER NOT NULL );";


//                                                // Set up the location column as a foreign key to location table.
//                                                " FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
//                                                MovieContract.ReviewEntry.TABLE_NAME + " (" + MovieContract.ReviewEntry._ID + "), " +

        // To assure the application have just one weather entry per day
        // per location, it's created a UNIQUE constraint with REPLACE strategy
//                                                " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

