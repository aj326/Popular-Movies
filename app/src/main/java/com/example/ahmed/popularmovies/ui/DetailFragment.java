package com.example.ahmed.popularmovies.ui;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.adapters.CursorTrailerAdapter;
import com.example.ahmed.popularmovies.pojo.MovieReviews;
import com.example.ahmed.popularmovies.pojo.MovieVideos;
import com.example.ahmed.popularmovies.pojo.Review;
import com.example.ahmed.popularmovies.pojo.Video;
import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.retrofit.ReviewsFetchService;
import com.example.ahmed.popularmovies.retrofit.TrailersFetchService;
import com.example.ahmed.popularmovies.utils.Constants;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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


    private Uri mUri, reviewUri, trailerUri;
    private boolean hasLoaded;
    private ViewGroup mTrailerViews;
    private String mShareTrailer;
    private String mMovieName;
    private ViewGroup mHeader;
    private long mId;
    private CheckBox isFav;
    String LOG_TAG = DetailFragment.class.getSimpleName();

    //    private CursorDetailAdapter mCursorDetailAdapter;
    private ListView mReviewList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Constants.DETAIL_URI);
            mMovieName = arguments.getString(Constants.MOVIE_NAME);
            mId = Long.parseLong(MovieContract.MovieEntry.getMovieIdFromUri(mUri));
            reviewUri = MovieContract.MovieEntry.buildMovieIdWithReview(mId);
            trailerUri = MovieContract.MovieEntry.buildMovieIdWithTrailer(mId);
            Log.d("reviewUri", reviewUri.toString());
            Log.d("trailerUri", trailerUri.toString());
            Log.d("mMovieName", mMovieName);

        }
        super.onCreate(savedInstanceState);
    }

    private LinearLayout mTrailerList;
    private SimpleCursorAdapter mReviewAdapter;
    private CursorTrailerAdapter mTrailerAdapter;

    public DetailFragment() {
//        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        mReviewHash = new HashMap<String, String>();
        super.onActivityCreated(savedInstanceState);
        insertTrailers();


        insertReviews();
        getLoaderManager().initLoader(Constants.DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(Constants.TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(Constants.REVIEW_LOADER, null, this);







    }

    private void insertTrailers() {
        final ArrayList<String> trailers = new ArrayList<>();
        final TrailersFetchService trailersFetchService = Constants.retrofit.create(
                TrailersFetchService.class);

        Call<MovieVideos> call = trailersFetchService.trailerList(mId);


        Log.d(LOG_TAG, "enqueuing trailers call");
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
                    int numberOfVideos = videos.size();
                    Vector<ContentValues> cVVector = new Vector<ContentValues>(numberOfVideos);

                    if (numberOfVideos > 0) {
                        for (Video video : videos) {
                            if (video.getType().equals("Trailer") && video.getSite().equals(
                                    "YouTube")) {
                                ContentValues value = new ContentValues();
                                Log.d(LOG_TAG, "Adding" + video.getName());
                                value.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
                                          video.getName());
                                value.put(MovieContract.TrailerEntry.COLUMN_URL, video.getKey());
                                value.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, mId);
                                cVVector.add(value);
                            }
                        }
                        if (cVVector.size() > 0) {
                            ContentValues[] cvArray = new ContentValues[cVVector.size()];
                            cVVector.toArray(cvArray);
                            getContext().getContentResolver().bulkInsert(trailerUri, cvArray);
                            Log.d(LOG_TAG, "inserted trailer values: " + cvArray.toString());
                        } else {
                            Log.v(LOG_TAG, "no trailers found");
                        }
                    }
                }
            }


            @Override
            public void onFailure(Throwable t) {


            }
        });
    }

    private void insertReviews() {
        final ArrayList<List<String>> cleanedUpReviews = new ArrayList<>();

        final ReviewsFetchService reviewsFetchService = Constants.retrofit.create(
                ReviewsFetchService.class);
        Call<MovieReviews> call = reviewsFetchService.reviewsList(
                mId);


        Log.d(LOG_TAG, "enqueuing reviews call");
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
                if (response.body() != null) {
                    MovieReviews movieReviews = response.body();
                    List<Review> reviews = movieReviews.getReviews();
                    Log.d(LOG_TAG, "looping reviews" + reviews.toString());
                    int numberOfReviews = reviews.size();
                    Vector<ContentValues> cVVector = new Vector<>(numberOfReviews);
                    if (numberOfReviews > 0) {
                        for (Review review : reviews) {
                            ContentValues value = new ContentValues();
                            value.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,
                                      review.getAuthor());
                            value.put(MovieContract.ReviewEntry.COLUMN_CONTENT,
                                      review.getContent());
                            value.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, mId);
                            cVVector.add(value);
                        }

                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        getContext().getContentResolver().bulkInsert(reviewUri, cvArray);

                        Log.d(LOG_TAG, "inserted review values:");

                    }
                } else {
                    Log.v(LOG_TAG, "no reviews found");
                }
            }


            @Override
            public void onFailure(Throwable t) {


            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        if (mUri == null) {
            return null;
        }
        Uri uri;
        String projection[];


        switch (id) {
            case Constants.DETAIL_LOADER: {
                uri = mUri;
                projection = null;
                break;
            }
            case Constants.REVIEW_LOADER: {
                uri = reviewUri;
                projection = new String[]{
                        MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
                        MovieContract.ReviewEntry.COLUMN_AUTHOR,
                        MovieContract.ReviewEntry.COLUMN_CONTENT,
                };
                break;
            }
            case Constants.TRAILER_LOADER: {
                uri = trailerUri;
                projection = new String[]{
                        MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
                        MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
                        MovieContract.TrailerEntry.COLUMN_URL
                };
                break;
            }
            default:
                throw new IllegalArgumentException("Loader ID is incorrect!");

        }
        return new CursorLoader(
                getActivity(),
                uri,
                projection,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=? ",
                new String[]{Long.toString(mId)},
                null
        );
    }

    //TODO use a viewholder for views here? Generic class for both views
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//        Log.d(LOG_TAG,reviewUri+" "+MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?"+mId+ DatabaseUtils.dumpCursorToString(data));
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            Log.d(LOG_TAG, "false");
            return;
        }

        Log.d(LOG_TAG,
              reviewUri + " " + MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=? " + Long.toString(
                      mId) + DatabaseUtils.dumpCursorToString(data));

//        data.()

        switch (loader.getId()) {
            case Constants.DETAIL_LOADER:
                ImageView poster = (ImageView) getView().findViewById(R.id.poster);
                Picasso.with(getContext()).load(
                        data.getString(Constants.DETAIL_COLUMNS.POSTER.ordinal())).resizeDimen(
                        R.dimen.img_width, R.dimen.img_height)
                        .into(poster);

//                TextView title = (TextView) getView().findViewById(R.id.movie_title);
//                title.setText(data.getString(Constants.DETAIL_COLUMNS.TITLE.ordinal()));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                        data.getString(Constants.DETAIL_COLUMNS.TITLE.ordinal()) );
                TextView release_date = (TextView) getView().findViewById(R.id.movie_year);
                release_date.setText(
                        data.getString(Constants.DETAIL_COLUMNS.RELEASE_DATE.ordinal()));

                TextView plot = (TextView) getView().findViewById(R.id.plot);
                plot.setText(data.getString(Constants.DETAIL_COLUMNS.PLOT.ordinal()));

                TextView rating = (TextView) getView().findViewById(R.id.rating);
                rating.setText(data.getString(Constants.DETAIL_COLUMNS.RATING.ordinal()));


//        values.put(MovieColumns.IS_FAVORITE, isFav.isChecked() ? 1 : 0);
                isFav = (CheckBox) getView().findViewById(R.id.star);
                isFav.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, isChecked);
                        getActivity().getContentResolver().update(mUri, values,
                                                                  MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=? ",
                                                                  new String[]{Long.toString(mId)});
                    }
                });
                isFav.setChecked(
                        (data.getInt(Constants.DETAIL_COLUMNS.IS_FAVORITE.ordinal()) == 1));
                return;

            case Constants.REVIEW_LOADER:
                Log.v(LOG_TAG, "Review Loader");
                mReviewAdapter.changeCursor(data);
                return;

            case Constants.TRAILER_LOADER:
                Log.v(LOG_TAG, "Trailer Loader");
                if (mShareTrailer == null) {
                    mShareTrailer = data.getString(Constants.TRAILER_COLUMNS.NAME.ordinal())+": http://www.youtube.com/watch?v="
                                    +
                                    data.getString(Constants.TRAILER_COLUMNS.URL.ordinal());
                    setHasOptionsMenu(true);
                }
                if (!hasLoaded) {
                    do {
                        View icon = View.inflate(getContext(), R.layout.trailer_icon,
                                                 null).findViewById(R.id.list_item_trailer_icon);
                        TextView name = (TextView) View.inflate(getContext(), R.layout.trailer_name,
                                                                null).findViewById(
                                R.id.list_item_trailer_name);
                        name.setGravity(Gravity.CENTER_VERTICAL);
                        LinearLayout layout = (LinearLayout) View.inflate(getContext(),
                                                                          R.layout.layout_trailers,
                                                                          null);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(35, 0, 25, 0);
                        layoutParams.gravity = Gravity.CENTER_VERTICAL;

                        String index = data.getString(Constants.TRAILER_COLUMNS.NAME.ordinal());
                        String url = data.getString(Constants.TRAILER_COLUMNS.URL.ordinal());

                        name.setText(index);
                        icon.setTag(url);

                        icon.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                        "http://www.youtube.com/watch?v=" + v.getTag())));
                            }
                        });
                        Log.d(LOG_TAG, "populating listview");
                        layout.addView(icon, layoutParams);
                        layout.addView(name, layoutParams);
                        mHeader.addView(layout);
                    } while (data.moveToNext());
                    hasLoaded = true;
                    TextView ReviewLabel = new TextView(getContext());
                    ReviewLabel.setText(R.string.reviews_label);
                    ReviewLabel.setTextAppearance(getContext(),
                                                  android.R.style.TextAppearance_Large);
                    mHeader.addView(ReviewLabel);
                }
        }
    }


//        if (data.getInt(DETAIL_COLUMNS.IS_FAVORITE.ordinal()) == 1) {
//            Log.d("isFav", starred.isChecked() + "");
//            starred.setChecked(true);
//            starred.setButtonDrawable(android.R.drawable.btn_star_big_on);
//        }
    // If onCreateOptionsMenu has already happened, we need to update the share intent now.


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mReviewAdapter.swapCursor(null);

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//        LinearLayout rootview= (LinearLayout) inflater.inflate(R.layout.fragment_detail_header,
//                                                                   container,
//                                                               false);

        mHeader = (CoordinatorLayout) inflater.inflate(R.layout.fragment_detail_header,
                                                       mReviewList,
                                                       false);
        Toolbar toolbar = (Toolbar) mHeader.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(
                true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
//        toolbar.inflateMenu(R.menu.detailfragment);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.d(LOG_TAG,"invoked share click");
//
//                switch (item.getItemId()){
//                    case R.id.menu_item_share:
//                        ShareActionProvider mShareActionProvider =
//                                (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//                        if(mShareActionProvider!=null) {Log.d(LOG_TAG,"provider not null");}
//                        Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
//                        return true;
//                }
//
//                return false;
//            }
////                ShareActionProvider mShareActionProvider =
////                        (ShareActionProvider) MenuItemCompat.getActionProvider(item);
////
////                // Attach an intent to this ShareActionProvider.  You can update this at any time,
////                // like when the user selects a new piece of data they might like to share.
////                if (mShareActionProvider != null) {
////                    Log.d(LOG_TAG,"share action provider is not null");
////
////                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
////                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
////
////                    shareIntent.setType("text/plain");
////                    shareIntent.putExtra(Intent.EXTRA_TEXT, mShareTrailer + "#PopularMovies");
////                    mShareActionProvider.setShareIntent(shareIntent);
////                    return true;
////                }
////                return true;
////            }
//        });

//        View rootview = View.inflate(getContext(), R.layout.fragment_detail_header, mReviewList);
        mReviewList = (ListView) inflater.inflate(R.layout.listview_review, null, false);

        mReviewList.addHeaderView(mHeader);
        mHeader = (LinearLayout) mHeader.findViewById(R.id.fragment_detail_header_layout_container);
//        mTrailerList.addFooterView(mReviewList);
//        mReviewList.addHeaderView(header);
//        mReviewList.addFooterView(mTrailerList);


//        mTrailerAdapter = new ArrayAdapter<String>(getContext(),R.layout.list_item_trailer,)


        String[] columns = new String[]{
                MovieContract.ReviewEntry.COLUMN_AUTHOR,
                MovieContract.ReviewEntry.COLUMN_CONTENT
        };
        int[] to = new int[]{
                R.id.list_item_review_author,
                R.id.list_item_review_content
        };
        mReviewAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item_review,
                                                 null, columns, to, 0);
        mTrailerAdapter = new CursorTrailerAdapter(getContext(), null, 0);

        mReviewList.setAdapter(mReviewAdapter);
//        mTrailerList.setAdapter(mTrailerAdapter);
//        LinearLayout myContainer = (LinearLayout) rootview.findViewById(R.id.reviews_container);
//                myContainer.addView(mReviewList);


//        mCursorReviewAdapter = new CursorReviewAdapter(getContext(),null);
        Log.d(LOG_TAG, "set up mReviewList");


        return mReviewList;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(LOG_TAG,"onCreateOptionsMenu" + mMovieName);

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            Log.d(LOG_TAG, "onCreateOptionsMenu Share Action Provider");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieName + "—" + mShareTrailer + " " + Constants.HASHTAG_PROJECT);
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

}

