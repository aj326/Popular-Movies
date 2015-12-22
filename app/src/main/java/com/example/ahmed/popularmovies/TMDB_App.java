package com.example.ahmed.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ahmed on 12/2/15.
 */
public class TMDB_App extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
