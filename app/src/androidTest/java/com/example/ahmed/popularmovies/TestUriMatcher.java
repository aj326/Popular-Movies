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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.provider.MovieProvider;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_DIR = MovieContract.ReviewEntry.CONTENT_URI;
    private static final Uri TEST_TRAILER_DIR = MovieContract.TrailerEntry.CONTENT_URI;

    private static final Uri TEST_MOVIE_WITH_ID =MovieContract.MovieEntry.buildMovieUri(
            1234);
    private static final Uri TEST_MOVIE_WITH_ID_WITH_REVIEW =MovieContract.MovieEntry.buildMovieIdWithReview(
            1234);
    private static final Uri TEST_MOVIE_WITH_ID_WITH_TRAILER =MovieContract.MovieEntry.buildMovieIdWithTrailer(
            1234);


    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE DIR URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);

        assertEquals("Error: The TRAILER DIR URI was matched incorrectly.",
                     testMatcher.match(TEST_TRAILER_DIR), MovieProvider.TRAILER);

        assertEquals("Error: The REVIEW DIR URI was matched incorrectly.",
                     testMatcher.match(TEST_REVIEW_DIR), MovieProvider.REVIEW);

        assertEquals("Error: The MOVIE WITH ID was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_ID), MovieProvider.MOVIE_WITH_ID);

        assertEquals("Error: The MOVIE WITH ID WITH REVIEW was matched incorrectly.",
                     testMatcher.match(TEST_MOVIE_WITH_ID_WITH_REVIEW), MovieProvider.MOVIE_WITH_REVIEW);

        assertEquals("Error: The MOVIE WITH ID WITH TRAILER was matched incorrectly.",
                     testMatcher.match(TEST_MOVIE_WITH_ID_WITH_TRAILER), MovieProvider.MOVIE_WITH_TRAILER);

    }
}
