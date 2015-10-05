package com.example.ahmed.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.ahmed.popularmovies.R.id;
import com.example.ahmed.popularmovies.R.layout;
import com.example.ahmed.popularmovies.R.string;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopMovieGridFragment extends Fragment {

    private final String LOG_TAG = PopMovieGridFragment.class.getSimpleName();
    private GridViewAdapter mMoviesAdapter;
    private ArrayList<MyMovie> mGridData;
    private GridView mGridView;
    public PopMovieGridFragment() {
    }
    public void onStart() {
        this.updateMovies();
        super.onStart();
    }

    private boolean updateMovies() {
        PopMovieGridFragment.FetchMoviesTask moviesTask = new PopMovieGridFragment.FetchMoviesTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        this.mGridData.clear();
//        TODO sort popular movies by their ratings
        String sort = sharedPref.getString(this.getString(string.pref_sorting_key), this.getString(string.pop_desc));


//        Log.v(LOG_TAG, "Loc from SharedPref: "pop + location + "\tUnits from SharedPref: " + units + "\tmetricV: " + getString(R.string.metricV));
        moviesTask.execute(sort);
        return true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(id.gridview_movies);
        this.mGridData = new ArrayList<MyMovie>();
        this.mMoviesAdapter =
                new GridViewAdapter(
                        this.getActivity(), // The current context (this activity)
                        layout.movie_tiles, // The name of the layout ID.
                        this.mGridData);
        gridView.setAdapter(this.mMoviesAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MyMovie movie = PopMovieGridFragment.this.mMoviesAdapter.getItem(position);
                Intent detailActivityIntent = new Intent(PopMovieGridFragment.this.getActivity(), DetailActivity.class);
                detailActivityIntent.putExtras(movie.bundleMovie());
                PopMovieGridFragment.this.startActivity(detailActivityIntent);
            }
        });
        return rootView;

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {
        private String MY_API_KEY;
        private final String LOG_TAG = PopMovieGridFragment.FetchMoviesTask.class.getSimpleName();


        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void getMoviesFromJsonStr(String json)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.


            JSONObject movieJson = new JSONObject(json);
            Log.v(this.LOG_TAG, "movieJson VAL<>: " + movieJson);
            String OWM_RESULT = "results";
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);
//            Log.v(LOG_TAG, "movieArray VAL<>: " + movieArray.toString());
//            Log.v(LOG_TAG, "movieArray.getJSONObject(0).getString VAL<>: " + movieArray.getJSONObject(0).getString("poster_path").toString());

            int moviesNumber = movieArray.length();
            String OWM_OVERVIEW = "overview";
            String OWM_USER_RATING = "vote_average";
            String OWM_RELEASE_DATE = "release_date";
            String OWM_ORIGINAL_TITLE = "original_title";
            String OWM_POSTER_IMG = "poster_path";
            for (int i = 0; i < moviesNumber; i++) {
                JSONObject movieJSON = movieArray.getJSONObject(i);
                MyMovie movie = new MyMovie
                        (
                                movieJSON.getString(OWM_ORIGINAL_TITLE)
                                , movieJSON.getString(OWM_POSTER_IMG)
                                , movieJSON.getString(OWM_OVERVIEW)
                                , movieJSON.getString(OWM_USER_RATING)
                                , movieJSON.getString(OWM_RELEASE_DATE)
                        );
                PopMovieGridFragment.this.mGridData.add(movie);


//                Log.v(LOG_TAG, "Complete img path????????????????????"+ movies.get(i).getImageUrl());
            }
        Log.v(this.LOG_TAG, "mGridData> "+ PopMovieGridFragment.this.mGridData);
        }

        @Override
        protected Void doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                //http://api.themoviedb.org/3/discover/movie?api_key=0d2f78cd5f086e8d35e0274952749495&sort_by=popularity.desc
                String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                String API_KEY_PARAM = "api_key";
                String SORT_BY_PARAM = "sort_by";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, this.MY_API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .build();

                URL url = new URL(builtUri.toString());
//                Log.v(LOG_TAG, "*****************URL > " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(this.LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(this.LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                this.getMoviesFromJsonStr(movieJsonStr);
            } catch (JSONException e) {
                Log.e(this.LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            PopMovieGridFragment.this.mMoviesAdapter.setGridData(PopMovieGridFragment.this.mGridData);
                // New data is back from the server.  Hooray!
        }
    }
}
