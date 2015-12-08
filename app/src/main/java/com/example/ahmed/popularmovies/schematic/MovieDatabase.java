package com.example.ahmed.popularmovies.schematic;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by ahmed on 10/18/15.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase(){}

    public static final int VERSION = 1;


    @Table(MovieColumns.class) public static final String MOVIES = "movies";

}
