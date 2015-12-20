package com.example.ahmed.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ahmed.popularmovies.data.MovieContract;
import com.example.ahmed.popularmovies.rest.CursorReviewAdapter;
import com.example.ahmed.popularmovies.rest.MovieReviews;
import com.example.ahmed.popularmovies.rest.MovieReviewsFetchService;
import com.example.ahmed.popularmovies.rest.MovieVideos;
import com.example.ahmed.popularmovies.rest.Review;
import com.example.ahmed.popularmovies.rest.TrailersUrlFetchService;
import com.example.ahmed.popularmovies.rest.Video;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
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


    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;
    private static final int REVIEW_LOADER = 1;
    private Uri mUri, reviewUri;
    private long mId;
    private CheckBox isFav;
//    private CursorDetailAdapter mCursorDetailAdapter;
    private CursorReviewAdapter mCursorReviewAdapter;
    private ListView mReviewList;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    private HashMap<String,String> mReviewHash;



    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        mReviewHash = new HashMap<String, String>();
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);

//        insertTrailers();
        insertReviews();



        super.onActivityCreated(savedInstanceState);
    }
    private void insertTrailers() {
        final ArrayList<String> trailers = new ArrayList<>();
        final TrailersUrlFetchService trailersUrlFetchService = PopMovieGridFragment.retrofit.create(
                TrailersUrlFetchService.class);
//        Log.d(LOG_TAG, "query count: " + mContentResolver.query(
//                MoviesProvider.Movies.CONTENT_URI, null, null, null, null).getCount());

        Cursor query =  getContext().getContentResolver().query(
                mUri, null, null, null, null);
        if (!query.moveToFirst()) {
            Log.e(LOG_TAG, "cant query");
            return;
        }
        Call<MovieVideos> call = trailersUrlFetchService.trailerList(query.getInt(
                    COLUMNS.MOVIE_ID.ordinal()));


//        Log.d(LOG_TAG, "enqueing trailers call");
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
                    value.put(MovieContract.TrailerEntry.COLUMN_URL, trailers.toString());
                    getContext().getContentResolver().update(mUri,
                                                             value, null,
                                                             null);
//                    Log.d(LOG_TAG, "updated movie");
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
//        Log.d(LOG_TAG, "query count: " + mContentResolver.query(
//                MoviesProvider.Movies.CONTENT_URI, null, null, null, null).getCount());
//        ContentValues values = new ContentValues();
//        values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,mId);
//        mContentResolver.insert(reviewUri, values);
//        final Cursor query = mContentResolver.query(
//                reviewUri, null, null, null, null);
////        ContentValues value = new ContentValues();
////        value.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,"TEST");
////        value.put(MovieContract.ReviewEntry.COLUMN_CONTENT,"REVIEW");
////        value.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,Integer.parseInt(
////                reviewUri.getLastPathSegment()));
////        mContentResolver.insert(reviewUri,value);
//        if (!query.moveToFirst()) {
//            Log.e(LOG_TAG, "cant query review" + reviewUri.toString());
//            return;
//        }
        Call<MovieReviews> call = movieReviewsFetchService.reviewsList(
               mId);


        Log.d(LOG_TAG, "enqueing reviews call");
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
                        Log.d(LOG_TAG, "looping reviews"+ reviews.toString());
                        Vector<ContentValues> cVVector = new Vector<ContentValues>(reviews.size());

                        for (Review review : reviews) {
                            ContentValues value = new ContentValues();
                            value.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,review.getAuthor());
                            value.put(MovieContract.ReviewEntry.COLUMN_CONTENT,review.getContent());
                            value.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, mId);
                            cVVector.add(value);
//                            cleanedUpReviews.add(Arrays.asList(
//                                    review.getAuthor(), review.getContent()));
//                            mReviewHash.put(review.getAuthor(), review.getContent());

                        }
                        if ( cVVector.size() > 0) {
                            ContentValues[] cvArray = new ContentValues[cVVector.size()];
                            cVVector.toArray(cvArray);
                            getContext().getContentResolver().bulkInsert(reviewUri, cvArray);
                        }
                        Log.d(LOG_TAG,"inserted review values:");


//                        ContentValues value = new ContentValues();
//                        Type type = new TypeToken<Map<String, String>>(){}.getType();

//                        value.put(MovieColumns.REVIEWS, gson.toJson(mReviewHash, type));
//                        mContentResolver.update(mUri,
//                                                value, null, null);
//                        DatabaseUtils.dumpCursor(query);
//                        Log.d("Gson", mReviewHash.keySet().toString());
//                        BaseAdapter adapter = new HashMapAdapter(mReviewHash);
//                        mReviewRecyclerView.se/tAdapter(adapter);

//                        mReviewHash.clear();
//                        cleanedUpReviews.clear();

//                        mList.clear();
                    }
                else
                        Log.e(LOG_TAG, "response.body was null!");
            }


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
        Uri uri = id==REVIEW_LOADER? reviewUri:mUri;
        String projection[] = id==REVIEW_LOADER?
                new String[]{                     MovieContract.ReviewEntry.TABLE_NAME+"."+MovieContract.ReviewEntry._ID,
                     MovieContract.ReviewEntry.COLUMN_AUTHOR,
                     MovieContract.ReviewEntry.COLUMN_CONTENT,
        }:
                null;
        return new CursorLoader(
                getActivity(),
                uri,
                projection,
                MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=? ",
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
            case DETAIL_LOADER:
                ImageView poster = (ImageView) getView().findViewById(R.id.poster);
                Picasso.with(getContext()).load(
                        data.getString(COLUMNS.POSTER.ordinal())).resizeDimen(
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

                isFav.setChecked((data.getInt(COLUMNS.IS_FAVORITE.ordinal()) == 1));
//                mCursorDetailAdapter.swapCursor(data);

                Log.v(LOG_TAG, "Review Loader");
////                mCursorReviewAdapter.swapCursor(data);
////                mCursorReviewAdapter.notifyDataSetChanged();

                return;

            case REVIEW_LOADER:
                Log.v(LOG_TAG, "Review Loader");
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                        MovieContract.ReviewEntry._ID,
                                                                                               MovieContract.ReviewEntry.COLUMN_AUTHOR,
                                                                                               MovieContract.ReviewEntry.COLUMN_CONTENT});
//                mReviewRecyclerView.
                        do{
                            Log.d(LOG_TAG,"LOOOOOOOOOOOPING");
                            matrixCursor.addRow(new Object[]
                                    {
                                            data.getString(data.getColumnIndex(MovieContract.ReviewEntry._ID)),
                                    data.getString(data.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR)),
                                    data.getString(data.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT))}
                                                 );
                        }
                        while (data.moveToNext());
                DatabaseUtils.dumpCursor(matrixCursor);
                MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor, data });
                mSimpleCursorAdapter.changeCursor(mergeCursor);
                return;
               }
    }


//        if (data.getInt(COLUMNS.IS_FAVORITE.ordinal()) == 1) {
//            Log.d("isFav", starred.isChecked() + "");
//            starred.setChecked(true);
//            starred.setButtonDrawable(android.R.drawable.btn_star_big_on);
//        }
        // If onCreateOptionsMenu has already happened, we need to update the share intent now.


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mCursorReviewAdapter.swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LinearLayout rootview= (LinearLayout) inflater.inflate(R.layout.fragment_detail_header,
//                                                                   container,
//                                                               false);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.fragment_detail_header, mReviewList,
                                                        false);
//        View rootview = View.inflate(getContext(), R.layout.fragment_detail_header, mReviewList);
        mReviewList = (ListView) inflater.inflate(R.layout.listview_review,null,false);
        mReviewList.addHeaderView(header);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            mId = Long.parseLong(MovieContract.MovieEntry.getMovieIdFromUri(mUri));
            reviewUri = MovieContract.MovieEntry.buildMovieIdWithReview(mId);
            Log.d("reviewUri",reviewUri.toString());

        }
        isFav = (CheckBox)header.findViewById(R.id.star);
        isFav.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues values = new ContentValues();
//                Log.d("Det:onChecked", "before update");
                values.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, isChecked);
                getActivity().getContentResolver().update(mUri, values, null,
                                                          null);
//                Log.d("Det:onChecked", "after update");
            }
        });
//        mCursorDetailAdapter = new CursorDetailAdapter(getContext(),null);

//        mCursorDetailAdapter.bindView(isFav, getContext(), null);

        String[] columns = new String[]{
                MovieContract.ReviewEntry.COLUMN_AUTHOR,
                MovieContract.ReviewEntry.COLUMN_CONTENT
        };
        int[] to = new int[]{
                R.id.list_item_review_author,
                R.id.list_item_review_content
        };
        mReviewList.setDividerHeight(0);
//mReviewList = new
        mSimpleCursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.list_item_review,null,columns,to,0);
    mReviewList.setAdapter(mSimpleCursorAdapter);
//        LinearLayout myContainer = (LinearLayout) rootview.findViewById(R.id.reviews_container);
//                myContainer.addView(mReviewList);


//        mCursorReviewAdapter = new CursorReviewAdapter(getContext(),null);
        Log.d(LOG_TAG,"set up mReviewList");



        return mReviewList;
    }

    @Override
    public void onViewCreated(
            View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        _ID, MOVIE_ID, TITLE, POSTER, PLOT, RELEASE_DATE, POPULARITY, RATING, SORT_BY_RATING, IS_FAVORITE
    }
}

