package com.example.ahmed.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.adapters.CursorMovieAdapter;
import com.example.ahmed.popularmovies.utils.Constants;
import com.facebook.stetho.Stetho;

import static com.example.ahmed.popularmovies.adapters.ViewPagerAdapter.setupViewPager;

public class MainActivity extends AppCompatActivity implements CursorMovieAdapter.Callback {


    private static boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        this.setContentView(R.layout.activity_main);


        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, new DetailFragment())
                        .commit();
            }
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.movie_list_toolbar);
            setSupportActionBar(toolbar);
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(this, viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            mTwoPane = false;
        }

    }




    @Override
    public void onItemSelected(Uri movieUri, String movieName) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(Constants.DETAIL_URI, movieUri);
            args.putString(Constants.MOVIE_NAME, movieName);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, Constants.DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(movieUri);
            intent.putExtra(Constants.MOVIE_NAME, movieName);
            startActivity(intent);
        }
    }

}
