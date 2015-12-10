package com.example.ahmed.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.popularmovies.rest.CursorMovieAdapter;
import com.example.ahmed.popularmovies.rest.Movie;
import com.example.ahmed.popularmovies.rest.MoviesDetailsFetchService;
import com.example.ahmed.popularmovies.rest.MoviesFromTMDB;
import com.example.ahmed.popularmovies.schematic.MovieColumns;
import com.example.ahmed.popularmovies.schematic.MoviesProvider;
import com.example.ahmed.popularmovies.schematic.MoviesProvider.Movies;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 * <p>
 * TODO Sorting
 * TODO Trailers, Reviews
 * TODO Favorite
 * TODO restore scroll position in tablet
 */

public class PopMovieGridFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mPosition;
    private RecyclerView mRecyclerView;
    private static final int CURSOR_LOADER_ID = 0;
    private final String LOG_TAG = PopMovieGridFragment.class.getSimpleName();
    private static final String BASE_URL = "http://api.themoviedb.org";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build();
    private final String[] projections= {"ID","TRAILERS","REVIEWS"};
    ProgressDialog mProgressDialog;
    private CursorMovieAdapter mMoviesAdapter;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, getActivity().getContentResolver().toString());
        Cursor c = getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI,
                                                            null, null, null, null);
        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        if (c == null || c.getCount() == 0) {
            insertData();
        }




        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resume called");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }


    public void insertData() {
        Log.d(LOG_TAG, "insert");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(
                this.getActivity());
        final String SORT_KEY = sharedPref.getString(this.getString(R.string.pref_sorting_key),
                                                     this.getString(R.string.pop_desc));
        final int MAX_PAGES = 6;

        retrofit.client().networkInterceptors().add(new StethoInterceptor());
        MoviesDetailsFetchService service = retrofit.create(MoviesDetailsFetchService.class);
        for (int i = 1; i < MAX_PAGES; i++) {
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
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        MoviesFromTMDB moviesFromTMDB = response.body();
                        List<Movie> movies = moviesFromTMDB.getMovies();
                        Log.d("page: ", moviesFromTMDB.getPage().toString());
                        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<ContentProviderOperation>(
                                moviesFromTMDB.getMovies().size());
                        for (Movie movie : movies) {
                            Builder builder = ContentProviderOperation.newInsert(
                                    Movies.CONTENT_URI);
                            builder.withValue(MovieColumns.TITLE, movie.getTitle());
                            builder.withValue(MovieColumns.POPULARITY, movie.getPopularity());
                            builder.withValue(MovieColumns.RATING, movie.getVoteAverage());
                            builder.withValue(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
                            builder.withValue(MovieColumns.PLOT, movie.getOverview());
                            builder.withValue(MovieColumns.POSTER, movie.getImgUrl());
                            builder.withValue(MovieColumns.TRAILERS, "TEMP");
                            builder.withValue(MovieColumns.REVIEWS, "TEMP");
                            builder.withValue(MovieColumns.IS_FAVORITE, 0);
                            builder.withValue(MovieColumns.ID,movie.getId());


                            batchOperations.add(builder.build());
                        }

                        try {
                            getActivity().getContentResolver().applyBatch(MoviesProvider.AUTHORITY,
                                                                          batchOperations);
                        } catch (RemoteException | OperationApplicationException e) {
                            Log.e(LOG_TAG, "Error applying batch insert", e);
                        }

                    }
                }


                @Override
                public void onFailure(Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Log.getStackTraceString(t);

                }
            });
        }



    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(mRecyclerView.getContext(), 2)
        );
        mProgressDialog = new ProgressDialog(getContext());
        this.mMoviesAdapter =
                new CursorMovieAdapter(
                        getActivity(), // The current context (this activity)
                        null);

        mRecyclerView.setAdapter(this.mMoviesAdapter);
        return rootView;
    }

}
