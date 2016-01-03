package com.example.ahmed.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.popularmovies.R;

import static com.example.ahmed.popularmovies.adapters.ViewPagerAdapter.setupViewPager;

/**
 * Created by ahmed on 1/2/16.
 */
public class ToolbarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = View.inflate(getContext(), R.layout.tab_layout, null);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.movie_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        setupViewPager(this, viewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}
