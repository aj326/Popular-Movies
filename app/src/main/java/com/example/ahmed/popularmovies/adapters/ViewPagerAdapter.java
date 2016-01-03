package com.example.ahmed.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.ui.PopMovieGridFragment;
import com.example.ahmed.popularmovies.ui.ToolbarFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/3/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public static void setupViewPager(AppCompatActivity activity, ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                activity.getSupportFragmentManager());
        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC"), "Popular");
        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_SORT_BY_RATING + " DESC"), "Rating");
        adapter.addFragment(PopMovieGridFragment.newInstance("favorite"), "Favorite");
        viewPager.setAdapter(adapter);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public static void setupViewPager(ToolbarFragment fragment, ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                fragment.getChildFragmentManager());
        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC"), "Popular");
        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_SORT_BY_RATING + " DESC"), "Rating");
        adapter.addFragment(PopMovieGridFragment.newInstance("favorite"), "Favorite");
        viewPager.setAdapter(adapter);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
