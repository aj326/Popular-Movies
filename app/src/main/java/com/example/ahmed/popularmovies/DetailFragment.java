package com.example.ahmed.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ahmed.popularmovies.rest.CursorDetailAdapter;
import com.example.ahmed.popularmovies.schematic.MovieColumns;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 12/2/15.
 */
public class DetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;
    private Uri mUri;
    private CheckBox isFav;
    private CursorDetailAdapter mCursorDetailAdapter;
    private ListView mListView;



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
        if (mUri ==  null) {
            return null;
        }


//        Log.d("isFav", getActivity().getContentResolver().query(mUri,null,null,null,null).getInt(COLUMNS.IS_FAVORITE.ordinal()) + "");


        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                mUri,
                null,
                null,
                null,
                null
        );
    }
    //TODO use a viewholder for views here? Generic class for both views
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }

        ImageView poster = (ImageView) getView().findViewById(R.id.poster);
        Picasso.with(getContext()).load(data.getString(COLUMNS.POSTER.ordinal())).resizeDimen(
                R.dimen.img_width, R.dimen.img_height)
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


//        values.put(MovieColumns.IS_FAVORITE, isFav.isChecked() ? 1 : 0);
        Log.d(LOG_TAG, "onLoadFinished, isFav is " + isFav.isChecked());

//        isFav.setChecked((data.getInt(COLUMNS.IS_FAVORITE.ordinal()) == 1));

        mCursorDetailAdapter.swapCursor(data);






//        if (data.getInt(COLUMNS.IS_FAVORITE.ordinal()) == 1) {
//            Log.d("isFav", starred.isChecked() + "");
//            starred.setChecked(true);
//            starred.setButtonDrawable(android.R.drawable.btn_star_big_on);
//        }
        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorDetailAdapter.swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
        isFav = (CheckBox) inflater.inflate(R.layout.fragment_detail, container, false).findViewById(R.id.star);
        isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (((CheckBox) v).isChecked()) values.put(MovieColumns.IS_FAVORITE, 1);
                else values.put(MovieColumns.IS_FAVORITE, 0);
                getActivity().getContentResolver().update(mUri, values, null, null);
            }
        });
        mCursorDetailAdapter = new CursorDetailAdapter(getContext(),null);
        mCursorDetailAdapter.bindView(isFav, getContext(), null);



        return inflater.inflate(R.layout.fragment_detail, container, false);
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
    public enum COLUMNS {
        _ID, TITLE, POSTER, PLOT, RATING, POPULARITY, RELEASE_DATE, TRAILERS, REVIEWS, IS_FAVORITE
    }
}

