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
import com.example.ahmed.popularmovies.rest.MovieReviews;
import com.example.ahmed.popularmovies.rest.MovieReviewsFetchService;
import com.example.ahmed.popularmovies.rest.MovieVideos;
import com.example.ahmed.popularmovies.rest.Review;
import com.example.ahmed.popularmovies.rest.TrailersUrlFetchService;
import com.example.ahmed.popularmovies.rest.Video;
import com.example.ahmed.popularmovies.schematic.MovieColumns;
import com.example.ahmed.popularmovies.schematic.MoviesProvider;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

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
        insertTrailers();
        insertReviews();

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    private void insertTrailers() {
        final ArrayList<String> trailers = new ArrayList<>();
        final TrailersUrlFetchService trailersUrlFetchService = PopMovieGridFragment.retrofit.create(
                TrailersUrlFetchService.class);
        Log.d(LOG_TAG, "query count: " + getContext().getContentResolver().query(
                MoviesProvider.Movies.CONTENT_URI, null, null, null, null).getCount());

        Cursor query = getContext().getContentResolver().query(
                mUri, null, null, null, null);
        if (!query.moveToFirst()) {
            Log.e(LOG_TAG, "cant query");
            return;
        }
        Call<MovieVideos> call = trailersUrlFetchService.trailerList(query.getInt(
                    COLUMNS.ID.ordinal()));


        Log.d(LOG_TAG, "enqueing trailers call");
        call.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Response<MovieVideos> response, Retrofit retrofit) {
                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    Converter<ResponseBody, MovieVideos> errorConverter =
                            retrofit.responseConverter(MovieVideos.class, new Annotation[0]);
                    try {
                        MovieVideos error = errorConverter.convert(response.errorBody());
                        Log.e("response", error.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.body() != null) {
                    MovieVideos movieVideos = response.body();

                    List<Video> videos = movieVideos.getVideos();

                    for (Video video : videos) {

                        if (video.getType().equals("Trailer") && video.getSite().equals(
                                "YouTube")) {
                            trailers.add(video.getKey());
                        }
                    }
                    ContentValues value = new ContentValues();
                    value.put(MovieColumns.TRAILERS, trailers.toString());
                    getContext().getContentResolver().update(mUri,
                                                             value, null,
                                                             null);
                    Log.d(LOG_TAG, "updated movie");
                    trailers.clear();

                }
            }


            @Override
            public void onFailure(Throwable t) {


            }
        });
    }
    private void insertReviews() {
        final ArrayList<List<String>> cleanedUpReviews = new ArrayList<>();
        final MovieReviewsFetchService movieReviewsFetchService = PopMovieGridFragment.retrofit.create(MovieReviewsFetchService.class);
        Log.d(LOG_TAG, "query count: " + getContext().getContentResolver().query(
                MoviesProvider.Movies.CONTENT_URI, null, null, null, null).getCount());

        Cursor query = getContext().getContentResolver().query(
                mUri, null, null, null, null);
        if (!query.moveToFirst()) {
            Log.e(LOG_TAG, "cant query");
            return;
        }
        Call<MovieReviews> call = movieReviewsFetchService.reviewsList(query.getInt(
                COLUMNS.ID.ordinal()));


        Log.d(LOG_TAG, "enqueing trailers call");
        call.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Response<MovieReviews> response, Retrofit retrofit) {
                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    Converter<ResponseBody, MovieReviews> errorConverter =
                            retrofit.responseConverter(MovieReviews.class, new Annotation[0]);
                    try {
                        MovieReviews error = errorConverter.convert(response.errorBody());
                        Log.e("response", error.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    if(response.body() != null){
                        MovieReviews movieReviews = response.body();

                        List<Review> reviews = movieReviews.getReviews();

                        for (Review review : reviews) {
                            cleanedUpReviews.add(Arrays.asList(
                                    review.getAuthor(), review.getContent()));
                        }
                        ContentValues value = new ContentValues();
                        value.put(MovieColumns.REVIEWS, cleanedUpReviews.toString());
                                getContext().getContentResolver().update(mUri,
                                                                         value, null, null);
                        cleanedUpReviews.clear();
                    }}


                @Override
            public void onFailure(Throwable t) {


            }
        });
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
        _ID, TITLE, POSTER, PLOT, RATING, POPULARITY, RELEASE_DATE, TRAILERS, REVIEWS, IS_FAVORITE, ID
    }
}

