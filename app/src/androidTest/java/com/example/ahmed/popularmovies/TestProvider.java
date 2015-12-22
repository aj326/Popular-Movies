/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ahmed.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.ahmed.popularmovies.provider.MovieContract;

/*
    Note: This is not a complete set of tests of the Sunshine ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.

    Students: Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {
//
//    public static final String LOG_TAG = TestProvider.class.getSimpleName();
//
//    /*
//       This helper function deletes all records from both database tables using the ContentProvider.
//       It also queries the ContentProvider to make sure that the database has been successfully
//       deleted, so it cannot be used until the Query and Delete functions have been written
//       in the ContentProvider.
//
//       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
//       the delete functionality in the ContentProvider.
//     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0,
                     cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Location table during delete", 0,
                     cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Location table during delete", 0,
                     cursor.getCount());
        cursor.close();
    }
//
//
//    /*
//        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
//        you have implemented delete functionality there.
//     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }
//
//    // Since we want each test to start with a clean slate, run deleteAllRecords
//    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
//
//    /*
//        This test checks to make sure that the content provider is registered correctly.
//        Students: Uncomment this test to make sure you've correctly registered the MovieProvider.
//     */
//    public void testProviderRegistry() {
//        PackageManager pm = mContext.getPackageManager();
//
//        // We define the component name based on the package name from the context and the
//        // MovieProvider class.
//        ComponentName componentName = new ComponentName(mContext.getPackageName(),
//                                                        MovieProvider.class.getName());
//        try {
//            // Fetch the provider info using the component name from the PackageManager
//            // This throws an exception if the provider isn't registered.
//            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
//
//            // Make sure that the registered authority matches the authority from the Contract.
//            assertEquals(
//                    "Error: MovieProvider registered with authority: " + providerInfo.authority +
//                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
//                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
//        } catch (PackageManager.NameNotFoundException e) {
//            // I guess the provider isn't registered correctly.
//            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
//                       false);
//        }
//    }
//
//    /*
//            This test doesn't touch the database.  It verifies that the ContentProvider returns
//            the correct type for each type of URI that it can handle.
//            Students: Uncomment this test to verify that your implementation of GetType is
//            functioning correctly.
//         */
    public void testGetType() {
        // content://com.example.android.sunshine.app/movie/
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(
                "Error: the MovieContract.MovieEntry CONTENT_URI should return MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/movie/123
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildMovieUri(1234));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(
                "Error: the MovieContract.MovieEntry CONTENT_URI with location should return MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);


        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(MovieContract.ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                     MovieContract.ReviewEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(MovieContract.TrailerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                     MovieContract.TrailerEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildMovieIdWithReview(1234));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(
                "Error: the MovieContract.MovieEntry CONTENT_URI with location should return MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.ReviewEntry.CONTENT_ITEM_TYPE, type);


        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildMovieIdWithTrailer(1234));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(
                "Error: the MovieContract.MovieEntry CONTENT_URI with location should return MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.TrailerEntry.CONTENT_ITEM_TYPE, type);


    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
//    public void testBasicMovieWithReviewAndTrailerQuery() {
//        // insert our test records into the database
//        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//
//        long movieRowId;
//        ContentValues movieValues = TestUtilities.createMovieValues2();
//        movieRowId = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);
//
//        // Verify we got a row back.
//        assertTrue(movieRowId != -1);
//        ContentValues testValues1 = TestUtilities.createDummyReview(1234);
//        ContentValues testValues2 = TestUtilities.createDummyReview2(1234);
//        db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues1);
//        db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues2);
//        testValues1 = TestUtilities.createDummyTrailer(1234);
//        testValues2 = TestUtilities.createDummyTrailer2(1234);
//        db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, testValues1);
//        db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, testValues2);
//        db.close();
//
//        // Test the basic content provider query
//        Cursor movieCursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//        );
//
//        // Make sure we get the correct cursor out of the database
//        TestUtilities.validateCursor("testBasicMovieWithReviewAndTrailerQuery", movieCursor, movieValues);
//    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if your location queries are
        performing correctly.
     */
//    public void testBasicMovieQueries() {
//        // insert our test records into the database
//        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues testValues = TestUtilities.createMovieValues();
//        long movieRowId = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
//
//
//        // Test the basic content provider query
//        Cursor movieCursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//        );
//
//        // Make sure we get the correct cursor out of the database
//        TestUtilities.validateCursor("testBasicMovieQueries, movie query", movieCursor, testValues);
//
//        // Has the NotificationUri been set correctly? --- we can only test this easily against API
//        // level 19 or greater because getNotificationUri was added in API level 19.
//        if (Build.VERSION.SDK_INT >= 19) {
//            assertEquals("Error: Location Query did not properly set NotificationUri",
//                         movieCursor.getNotificationUri(), MovieContract.MovieEntry.CONTENT_URI);
//        }
//    }

//
//    /*
//        This test uses the provider to insert and then update the data. Uncomment this test to
//        see if your update location is functioning correctly.
//     */
    public void testUpdateMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMovieValues();

        Uri locationUri = mContext.getContentResolver().
                insert(MovieContract.MovieEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
//        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = TestUtilities.createMovieValues2();

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor locationCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        locationCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI, updatedValues, MovieContract.ReviewEntry._ID + "= ?",
                new String[] { Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        locationCursor.unregisterContentObserver(tco);
        locationCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry._ID + " = " + locationRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }
//
//
//    // Make sure we can still delete after adding/updating stuff
//    //
//    // Student: Uncomment this test after you have completed writing the insert functionality
//    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProviderReview() {
        ContentValues movieValues = TestUtilities.createMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, tco);
        Uri moviewUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId = ContentUris.parseId(moviewUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieContract.ReviewEntry.",
                cursor, movieValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues reviewValues = TestUtilities.createDummyReview(1234);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieContract.ReviewEntry.CONTENT_URI, true, tco);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(MovieContract.ReviewEntry.CONTENT_URI, reviewValues);
        assertTrue(weatherInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor reviewCursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor(
                "testInsertReadProvider. Error validating MovieContract.MovieEntry insert.",
                reviewCursor, reviewValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        reviewValues.putAll(movieValues);

        // Get the joined Weather and Location data
        reviewCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.buildMovieIdWithReview(movieRowId,reviewValues.getAsLong(
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID)),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        DatabaseUtils.dumpCursor(reviewCursor);
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data."+MovieContract.MovieEntry.buildMovieIdWithReview(movieRowId).toString(),
                reviewCursor, reviewValues);

    }
//    public void testInsertReadProviderTrailer() {
//        ContentValues movieValues = TestUtilities.createMovieValues();
//
//        // Register a content observer for our insert.  This time, directly with the content resolver
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, tco);
//        Uri moviewUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
//
//        // Did our content observer get called?  Students:  If this fails, your insert location
//        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        long movieRowId = ContentUris.parseId(moviewUri);
//
//        // Verify we got a row back.
//        assertTrue(movieRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieContract.ReviewEntry.",
//                                     cursor, movieValues);
//
//        // Fantastic.  Now that we have a location, add some weather!
//        ContentValues trailer = TestUtilities.createDummyTrailer(1234);
//        // The TestContentObserver is a one-shot class
//        tco = TestUtilities.getTestContentObserver();
//
//        mContext.getContentResolver().registerContentObserver(MovieContract.TrailerEntry.CONTENT_URI, true, tco);
//
//        Uri weatherInsertUri = mContext.getContentResolver()
//                .insert(MovieContract.TrailerEntry.CONTENT_URI, trailer);
//        assertTrue(weatherInsertUri != null);
//
//        // Did our content observer get called?  Students:  If this fails, your insert weather
//        // in your ContentProvider isn't calling
//        // getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        // A cursor is your primary interface to the query results.
//        Cursor trailerCursor = mContext.getContentResolver().query(
//                MovieContract.TrailerEntry.CONTENT_URI,  // Table to Query
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null // columns to group by
//        );
//
//        TestUtilities.validateCursor(
//                "testInsertReadProvider. Error validating MovieContract.MovieEntry insert.",
//                trailerCursor, trailer);
//
//        // Add the location values in with the weather data so that we can make
//        // sure that the join worked and we actually get all the values back
//        trailer.putAll(movieValues);
//
//        // Get the joined Weather and Location data
//        trailerCursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.buildMovieIdWithTrailer(movieRowId,trailer.getAsLong(
//                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID)),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        DatabaseUtils.dumpCursor(trailerCursor);
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data."+MovieContract.MovieEntry.buildMovieIdWithReview(movieRowId,trailer.getAsLong(
//                                             MovieContract.TrailerEntry.COLUMN_MOVIE_ID)).toString(),
//                                     trailerCursor, trailer);
//
//    }
//
    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecordsReview() {
        testInsertReadProviderReview();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.ReviewEntry.CONTENT_URI,
                                                              true, locationObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, weatherObserver);

        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, trailerObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        locationObserver.waitForNotificationOrFail();
        weatherObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(locationObserver);
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
        mContext.getContentResolver().unregisterContentObserver(trailerObserver);

    }


//
//
//    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
//    // in your provider.  Note that this test will work with the built-in (default) provider
//    // implementation, which just inserts records one-at-a-time, so really do implement the
//    // BulkInsert ContentProvider function.
//    public void testBulkInsert() {
//        // first, let's create a location value
//        ContentValues testValues = TestUtilities.createMovieValues();
//        Uri locationUri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.CONTENT_URI, testValues);
//        long locationRowId = ContentUris.parseId(locationUri);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testBulkInsert. Error validating MovieContract.ReviewEntry.",
//                cursor, testValues);
//
//        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
//        // entries.  With ContentProviders, you really only have to implement the features you
//        // use, after all.
//        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues(locationRowId);
//
//        // Register a content observer for our bulk insert.
//        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, weatherObserver);
//
//        int insertCount = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, bulkInsertContentValues);
//
//        // Students:  If this fails, it means that you most-likely are not calling the
//        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
//        // ContentProvider method.
//        weatherObserver.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
//
//        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
//
//        // A cursor is your primary interface to the query results.
//        cursor = mContext.getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                MovieContract.MovieEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
//        );
//
//        // we should have as many records in the database as we've inserted
//        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
//
//        // and let's make sure they match the ones we created
//        cursor.moveToFirst();
//        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
//            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating MovieContract.MovieEntry " + i,
//                    cursor, bulkInsertContentValues[i]);
//        }
//        cursor.close();
//    }
}
