package com.example.ahmed.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopMovieGridFragment extends Fragment {

    private final String LOG_TAG = PopMovieGridFragment.class.getSimpleName();

    private ImageAdapter

    public PopMovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
