package com.example.ahmed.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.adapters.CursorMovieAdapter;
import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.utils.Constants;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements CursorMovieAdapter.Callback {
    public boolean mTwoPane;
    private Toolbar toolbar;
    private TabLayout tabLayout;

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
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
//                                                            MainActivity.this));
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
        //check if one or two panes
        if(findViewById(R.id.movie_detail_container)!=null)
        {
    mTwoPane=true;
            //check if already loaded then destroyed by an event like screen rotation
            if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new DetailFragment())
                    .commit();}
        }
        else
            mTwoPane=false;

    }
//    @Override
//    public void onBackPressed() {
//        if(getFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();
//        }
//        else {
//            getFragmentManager().popBackStack();
//        }
//    }
    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        Bundle b =new Bundle();

        b.putString("sorting", MovieContract.MovieEntry.COLUMN_POPULARITY+" DESC");
        ActionBar.Tab popularTab = actionBar
                .newTab()
                .setText("Popular")
//                .setIcon(R.drawable.ic_home)
                .setTabListener(
                        new SupportFragmentTabListener<PopMovieGridFragment>(
                                R.id.movie_list_fragment, this,
                                "popular",
                                PopMovieGridFragment.class, b));


        actionBar.addTab(popularTab);
        actionBar.selectTab(popularTab);
        b = new Bundle();
        b.putString("sorting", MovieContract.MovieEntry.COLUMN_SORT_BY_RATING + " DESC");

        ActionBar.Tab ratingTab = actionBar
                .newTab()
                .setText("Rating")
//                .setIcon(R.drawable.ic_home)
                .setTabListener(
                        new SupportFragmentTabListener<PopMovieGridFragment>(
                                R.id.movie_list_fragment, this,
                                "rating",
                                PopMovieGridFragment.class,b));

        actionBar.addTab(ratingTab);


        b = new Bundle();
        b.putString("sorting", "favorite");

        ActionBar.Tab favTAb = actionBar
                .newTab()
                .setText("Favorite")
//                .setIcon(R.drawable.ic_home)
                .setTabListener(
                        new SupportFragmentTabListener<PopMovieGridFragment>(
                                R.id.movie_list_fragment, this,
                                "favorite",
                                PopMovieGridFragment.class,b));

        actionBar.addTab(favTAb);
//        actionBar.selectTab(ratingTab);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri movieUri) {
        if(mTwoPane)
        {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            Log.d("MainAct movieUri", movieUri.toString());
            args.putParcelable(Constants.DETAIL_URI, movieUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, Constants.DETAILFRAGMENT_TAG)
                    .commit();
            Log.v("MainActivity","Completed replacing detail cont");
        } else {
            Log.d("MainAct movieUri", movieUri.toString());
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(movieUri);
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
