package com.example.ahmed.popularmovies;

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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final int DETAIL_LOADER = 0;


        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.v(LOG_TAG, "In onCreateLoader");
            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.v(LOG_TAG, "In onLoadFinished");
            if (!data.moveToFirst()) {
                return;
            }
            ImageView poster = (ImageView) getView().findViewById(R.id.poster);
            Picasso.with(getContext()).load(data.getString(COLUMNS.POSTER.ordinal()))
                    .into(poster);

            TextView title = (TextView) getView().findViewById(R.id.movie_title);
            title.setText(data.getString(COLUMNS.TITLE.ordinal()));

            getActivity().setTitle(data.getString(COLUMNS.TITLE.ordinal()));

            TextView release_date = (TextView) getView().findViewById(R.id.movie_year);
            release_date.setText(data.getString(COLUMNS.RELEASE_DATE.ordinal()));

            TextView plot = (TextView) getView().findViewById(R.id.plot);
            plot.setText(data.getString(COLUMNS.PLOT.ordinal()));

            TextView rating = (TextView) getView().findViewById(R.id.rating);
            rating.setText(data.getString(COLUMNS.RATING.ordinal()));

            CheckBox starred = (CheckBox) getView().findViewById(R.id.star);
            if (data.getInt(COLUMNS.IS_FAVORITE.ordinal()) == 1) {
                Log.d("isFav", starred.isChecked() + "");
                starred.setChecked(true);
                starred.setButtonDrawable(android.R.drawable.btn_star_big_on);
            }
            // If onCreateOptionsMenu has already happened, we need to update the share intent now.

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            return rootView;
        }

        //        TODO implement share
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
//            inflater.inflate(R.menu.detailfragment, menu);
//
//            // Retrieve the share menu item
//            MenuItem menuItem = menu.findItem(R.id.menu_item_share);
//
//            // Get the provider and hold onto it to set/change the share intent.
//            ShareActionProvider mShareActionProvider =
//                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//            // Attach an intent to this ShareActionProvider.  You can update this at any time,
//            // like when the user selects a new piece of data they might like to share.
//            if (mShareActionProvider != null ) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//            }
        }

        /*
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String PLOT = "plot";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TRAILERS = "trailers";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String REVIEWS = "reviews";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String IS_FAVORITE = "is_favorite";
         */
        private enum COLUMNS {
            _ID, TITLE, POSTER, PLOT, RATING, POPULARITY, RELEASE_DATE, TRAILERS, REVIEWS, IS_FAVORITE
        }
    }
}

/*
        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastStr + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
*/


