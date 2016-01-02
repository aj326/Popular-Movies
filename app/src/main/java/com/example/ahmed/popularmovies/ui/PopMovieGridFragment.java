package com.example.ahmed.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.adapters.CursorMovieAdapter;
import com.example.ahmed.popularmovies.pojo.Movie;
import com.example.ahmed.popularmovies.pojo.MoviesFromTMDB;
import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.retrofit.DetailsFetchService;
import com.example.ahmed.popularmovies.utils.Constants;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * TODO Sorting
 * TODO restore scroll position
 * TODO document!
 * TODO refactor!
 */

public class PopMovieGridFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    private  RecyclerView mRecyclerView;
//     ProgressDialog progressDialog = new ProgressDialog();
    private final String LOG_TAG = PopMovieGridFragment.class.getSimpleName();
//    ProgressDialog  mProgressDialog;
    private  CursorMovieAdapter mMoviesAdapter;
//    private boolean isFav = getSupportFragmentManager().findFragmentByTag()



//    private static final String ARG_SELECTION = "ARG_SELECTION";
    private String mSorting;
    private  String mSelection="";
    private  String[] mSelectionArg;
    private Bundle mBundle;
    private SwipeRefreshLayout swipeContainer;





    public static PopMovieGridFragment newInstance(String sorting) {
        PopMovieGridFragment f = new PopMovieGridFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_SORTING, sorting);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated called");
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                insertData(false);


            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                                               android.R.color.holo_green_light,

                                               android.R.color.holo_orange_light,

                                               android.R.color.holo_red_light);




        Cursor c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                                            null, null, null, null);
        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        if(c.getCount()==0)
            insertData(true);
        c.close();
//        if (savedInstanceState==null)
//        {
//            Log.d(LOG_TAG, "savedInstance is null"+ mSelection+ " "+ mSorting);
//
//            Bundle bundle = new Bundle();
//            bundle.putString(ARG_SELECTION,mSelection);
//            bundle.putString(ARG_SORTING,mSorting);
//            getLoaderManager().restartLoader(Constants.CURSOR_LOADER_ID, bundle,
//                                             this);
//
//        }
//        else {
//            Log.d(LOG_TAG, "savedInstance is not null"+ savedInstanceState.getString(ARG_SELECTION)+ " "+ savedInstanceState.getString(ARG_SORTING));
//            getLoaderManager().restartLoader(Constants.CURSOR_LOADER_ID, savedInstanceState, this);
//        }
        if(mBundle!=null)
        getLoaderManager().initLoader(Constants.CURSOR_LOADER_ID, mBundle,
                                      this);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(LOG_TAG, "resume called");
////        if(getArguments()!=null){
//////         mSorting =getArguments().getString(ARG_SORTING);
////
////        }
////            if (mSorting == null){
////                mSelection=MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "=? ";
////                mSelectionArg = new String[]{"1"};
////            }
////
//////            Log.d(LOG_TAG, mSorting);
////
////        }
//        mArgs = new Bundle(getArguments());
//        getLoaderManager().restartLoader(Constants.CURSOR_LOADER_ID, mArgs, this);
//
//    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        if(mSorting!=null)
//        Log.d(LOG_TAG,"Args: "+mSorting+" ");

//        else Log.d(LOG_TAG,"All is null!");
//        if(args!=null){
//        mSelection = args.getString(ARG_SELECTION);
        if(args!=null)
        mSorting = args.getString(Constants.ARG_SORTING);
        Log.d(LOG_TAG,"onCreateLoader(): "+ mSorting + mSelection);
        if (mSorting!=null && mSorting.equals( "favorite")){
            Log.d(LOG_TAG, "onCreateLoader(): favorites ");
//            mRecyclerView.swapAdapter(null,true);
//mMoviesAdapter.notifyItemRangeRemoved(0,mMoviesAdapter.getItemCount());
            return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
                                    null,
                                    MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "=? ",
                                    new String[]{"1"},
                                    null);
        }
        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                mSorting);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        DatabaseUtils.dumpCursor(data);
        mMoviesAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }


    private  void insertData(final boolean isFirstTime) {
        Log.d(LOG_TAG, "insert");

//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(
                this.getActivity());
        final String SORT_KEY = sharedPref.getString(this.getString(R.string.pref_sorting_key),
                                                     this.getString(R.string.pop_desc));
        final int MAX_PAGES = 6;

        Constants.retrofit.client().networkInterceptors().add(new StethoInterceptor());
        DetailsFetchService service = Constants.retrofit.create(DetailsFetchService.class);


        for (int i = 1; i < MAX_PAGES; i++) {
            final ProgressDialog progressDialog =  ProgressDialog.show(getContext(),
                                                                       "Fetching Movies",
                                                                       "Please wait...", false,
                                                                       true);
            Call<MoviesFromTMDB> call = service.movieList(i);

            call.enqueue(new Callback<MoviesFromTMDB>() {

                @Override
                public void onResponse(Response<MoviesFromTMDB> response, Retrofit retrofit) {

                    if (response != null && !response.isSuccess() && response.errorBody() != null) {
                        Converter<ResponseBody, MoviesFromTMDB> errorConverter =
                                retrofit.responseConverter(MoviesFromTMDB.class, new Annotation[0]);
                        try {
                            MoviesFromTMDB error = errorConverter.convert(response.errorBody());
                            Log.e("response", error.toString());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();

                        MoviesFromTMDB moviesFromTMDB = response.body();
                        List<Movie> movies = moviesFromTMDB.getMovies();
                        Log.d("page: ", moviesFromTMDB.getPage().toString());
                        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<ContentProviderOperation>(
                                moviesFromTMDB.getMovies().size());
                        for (Movie movie : movies) {
                            Builder builder = ContentProviderOperation.newInsert(
                                    MovieContract.MovieEntry.CONTENT_URI);
                            builder.withValue(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_SORT_BY_RATING, movie.getTrueRating());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_PLOT, movie.getOverview());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_POSTER, movie.getImgUrl());
                            builder.withValue(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 0);

                            builder.withValue(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
//                            ContentValues movie_id = new ContentValues();
//                            movie_id.put(ReviewColumns.MOVIE_ID,movie.getId());
//                            getActivity().getContentResolver().insert(MoviesProvider.Reviews.CONTENT_URI,movie_id);
                            batchOperations.add(builder.build());
                        }

                        try {
                            getActivity().getContentResolver().applyBatch(MovieContract.CONTENT_AUTHORITY,
                                                                          batchOperations);
                        } catch (RemoteException | OperationApplicationException e) {
                            Log.e(LOG_TAG, "Error applying batch insert", e);
                        }

                    }
                    if(!isFirstTime)
                    swipeContainer.setRefreshing(false);
                }


                @Override
                public void onFailure(Throwable t) {
                    Log.getStackTraceString(t);

                }
            });
        }


    }

    @Override
    public void onViewCreated(
            View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated called");
//        Bundle bundle = savedInstanceState!=null?savedInstanceState:getArguments();
        if (getArguments()!=null){
//            mSelection = bundle.getString(ARG_SELECTION);
            mSorting = getArguments().getString(Constants.ARG_SORTING);
            mBundle = getArguments();
            Log.d(LOG_TAG, "onViewCreated Args: " + mSorting);
            for (String key : getArguments().keySet())
            {
                Log.d("Bundle Debug", key + " = \"" + getArguments().get(key) + "\"");
            }        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(
                R.id.swipe_container);

        mRecyclerView.setLayoutManager(
                new GridLayoutManager(mRecyclerView.getContext(), 2)
        );
        this.mMoviesAdapter =
                new CursorMovieAdapter(
                        getActivity(), // The current context (this activity)
                        null);

        mRecyclerView.setAdapter(this.mMoviesAdapter);
        return rootView;
    }

}
