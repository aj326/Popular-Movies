package com.example.ahmed.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 * <p/>
 * TODO Progress Bar
 */

public class PopMovieGridFragment extends Fragment {

    private final String LOG_TAG = PopMovieGridFragment.class.getSimpleName();
    private final String BASE_URL = "http://api.themoviedb.org";
    private GridViewAdapter mMoviesAdapter;
    private boolean already_queried;
    private String sort;
    private List<Movie> mGridData;

    @Override
    public void onResume() {
        this.updateMovies();
        super.onResume();
    }

    public void updateMovies() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        final String SORT_KEY = sharedPref.getString(this.getString(R.string.pref_sorting_key), this.getString(R.string.pop_desc));
        if (this.already_queried) {
//            Log.d(LOG_TAG, already_queried + " SortKey " + SORT_KEY.equals(sort));
            if (!this.sort.equals(SORT_KEY)) {
                if (SORT_KEY.equals(this.getString(R.string.pop_desc))) {

                    Collections.sort(this.mGridData, new CompareByPop());
//                    Log.d(LOG_TAG, "compare by Pop: " + mGridData);

                } else {
                    Collections.sort(this.mGridData, new CompareByRating());
//                    Log.d(LOG_TAG, "compare by Rating: " + mGridData);
                }
            }
            this.mMoviesAdapter.setGridData((ArrayList<Movie>) this.mGridData);
            return;
        }
        this.sort = SORT_KEY;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        TMDBFetchService service = retrofit.create(TMDBFetchService.class);
        Call<MoviesFromTMDB> call = service.movieList(SORT_KEY);
        this.already_queried = true;
        call.enqueue(new Callback<MoviesFromTMDB>() {
            @Override
            public void onResponse(Response<MoviesFromTMDB> response, Retrofit retrofit) {
                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    Converter<ResponseBody, MoviesFromTMDB> errorConverter =
                            retrofit.responseConverter(MoviesFromTMDB.class, new Annotation[0]);
                    try {
                        MoviesFromTMDB error = errorConverter.convert(response.errorBody());
//                        Log.e("response", error.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    PopMovieGridFragment.this.mGridData.clear();
                    PopMovieGridFragment.this.mGridData.addAll(response.body().getMovies());
                    if (SORT_KEY.equals(PopMovieGridFragment.this.getString(R.string.rate_desc))) {
                        Collections.sort(PopMovieGridFragment.this.mGridData, new CompareByRating());
                    }
                    PopMovieGridFragment.this.mMoviesAdapter.setGridData((ArrayList<Movie>) PopMovieGridFragment.this.mGridData);
//                    Log.d("@onResponse", String.valueOf(mMoviesAdapter.getCount()));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.getStackTraceString(t);

            }
        });
        return;
//        Log.d("Done Update Movies: ", mGridData.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        this.mGridData = new ArrayList<>();

        this.mMoviesAdapter =
                new GridViewAdapter(
                        this.getActivity(), // The current context (this activity)
                        R.layout.movie_tiles, // The name of the layout ID.
                        (ArrayList<Movie>) this.mGridData);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = PopMovieGridFragment.this.mMoviesAdapter.getItem(position);
                Intent detailActivityIntent = new Intent(PopMovieGridFragment.this.getActivity(), DetailActivity.class);
                detailActivityIntent.putExtras(movie.bundleMovie());
                PopMovieGridFragment.this.startActivity(detailActivityIntent);
            }
        });
        this.updateMovies();
        gridView.setAdapter(this.mMoviesAdapter);
        return rootView;

    }

    public class CompareByRating implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return rhs.getVoteCount().compareTo(lhs.getVoteCount());
        }
    }

    public class CompareByPop implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return rhs.getPopularity().compareTo(lhs.getPopularity());
        }
    }



}
