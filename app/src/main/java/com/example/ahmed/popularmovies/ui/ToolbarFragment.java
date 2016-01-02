package com.example.ahmed.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.provider.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/2/16.
 */
public class ToolbarFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_layout, null);
        toolbar = (Toolbar) root.findViewById(R.id.movie_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getChildFragmentManager());
        Fragment fragment = PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC");
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.movie_list_container, fragment)
//                .commit();

        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC"), "Popular");



//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.movie_list_container, fragment)
//                .commit();

        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_SORT_BY_RATING+ " DESC"), "Rating");

        fragment = PopMovieGridFragment.newInstance("favorite");
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.movie_list_container, fragment)
//                .commit();
        adapter.addFragment(PopMovieGridFragment.newInstance("favorite"), "Favorite");
//        adapter.instantiateItem()
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
//             getSupportFragmentManager().beginTransaction()
//            .add(R.id.movie_list_container, mFragmentList.get(position)).commit();
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
