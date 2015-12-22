package com.example.ahmed.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.provider.MovieDBHelper;
import com.example.ahmed.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 1234);
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "PLOOOOOOT");
        movieValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 1.2);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, 1.3);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "dd");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "65");
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Asteroids");
        return movieValues;
    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the WeatherContract.
     */

    static ContentValues createDummyReview(long id) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,id);
        testValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Ahmed");
        testValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "This is a review");
        assertTrue(String.valueOf("review _ID dne movie_id," + testValues.getAsInteger(
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID) + " " + id + " " + id == testValues.getAsInteger(
                           MovieContract.ReviewEntry.COLUMN_MOVIE_ID)+""),
                   testValues.getAsInteger(MovieContract.ReviewEntry.COLUMN_MOVIE_ID) == id);
        return testValues;
    }

    static ContentValues createDummyTrailer(long id) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,id);
        testValues.put(MovieContract.TrailerEntry.COLUMN_URL, "Ahmed");
        assertTrue(String.valueOf("review _ID dne movie_id," + testValues.getAsInteger(
                           MovieContract.TrailerEntry.COLUMN_MOVIE_ID) + " " + id + " " + id == testValues.getAsInteger(
                           MovieContract.TrailerEntry.COLUMN_MOVIE_ID) + ""),
                   testValues.getAsInteger(MovieContract.TrailerEntry.COLUMN_MOVIE_ID) == id);
        return testValues;
    }
    static ContentValues createDummyTrailer2(long id) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,id);
        testValues.put(MovieContract.TrailerEntry.COLUMN_URL, "Jim");
        assertTrue(String.valueOf("review _ID dne movie_id," + testValues.getAsInteger(
                           MovieContract.TrailerEntry.COLUMN_MOVIE_ID) + " " + id + " " + id == testValues.getAsInteger(
                           MovieContract.TrailerEntry.COLUMN_MOVIE_ID) + ""),
                   testValues.getAsInteger(MovieContract.TrailerEntry.COLUMN_MOVIE_ID) == id);
        return testValues;
    }
    static ContentValues createDummyReview2(long id) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,id);
        testValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Faisal");
        testValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "This is another review");

        return testValues;
    }
    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", movieRowId != -1);

        return movieRowId;
    }

    public static ContentValues createMovieValues2() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 12345);
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "PLOOOOOOT");
        movieValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 1.2);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, 1.3);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "dd");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "65");
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Asteroids2");
        return movieValues;
    }


    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
