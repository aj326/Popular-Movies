package com.example.ahmed.popularmovies.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by ahmed on 12/25/15.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Popular", "Ranking", "Favorites" };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int sorting) {
        Log.d("getItem", sorting+"");
//        return PopMovieGridFragment.newInstance(sorting+1);
        return null;
    }

    @Override
    public CharSequence getPageTitle(int sorting) {
        // Generate title based on item sorting
        return tabTitles[sorting];
    }
}