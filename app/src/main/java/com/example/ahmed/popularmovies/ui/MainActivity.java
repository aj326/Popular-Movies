package com.example.ahmed.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.adapters.CursorMovieAdapter;
import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.utils.Constants;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CursorMovieAdapter.Callback {
    public boolean mTwoPane;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
//        setupTabs();
        this.setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            //check if already loaded then destroyed by an event like screen rotation
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, new DetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(PopMovieGridFragment.newInstance(
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC"), "Popular");


        adapter.addFragment(PopMovieGridFragment.newInstance(MovieContract.MovieEntry.COLUMN_SORT_BY_RATING + " DESC"),"Rating");

        adapter.addFragment(PopMovieGridFragment.newInstance("favorite"),"Favorite");

//        adapter.addFragment(new PopMovieGridFragment(), "THREE");
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



        //check if one or two panes


    //    @Override
//    public void onBackPressed() {
//        if(getFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();
//        }
//        else {
//            getFragmentManager().popBackStack();
//        }
//    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        this.getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            this.startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemSelected(Uri movieUri,String movieName) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            Log.d("MainAct movieUri", movieUri.toString());
            args.putParcelable(Constants.DETAIL_URI, movieUri);
            args.putString(Constants.MOVIE_NAME, movieName);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, Constants.DETAILFRAGMENT_TAG)
                    .commit();
            Log.v("MainActivity", "Completed replacing detail cont");
        } else {
            Log.d("MainAct movieUri", movieUri.toString());
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(movieUri);
            intent.putExtra(Constants.MOVIE_NAME,movieName);
            startActivity(intent);
        }
    }


//    @Override
//    public void changeFav(long _id, View view) {
//        ContentValues values = new ContentValues();
//        values.put(MovieColumns.IS_FAVORITE, ((CheckBox) view).isChecked());
//        getContentResolver().update(MoviesProvider.Movies.withId(_id), values, "movie_id=?",
//                                    new String[]{
//                                            MoviesProvider.Movies.withId(_id).getLastPathSegment()});
//    }

}
