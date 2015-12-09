package com.example.ahmed.popularmovies.schematic;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by ahmed on 10/18/15.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";


    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";


    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String PLOT = "plot";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TRAILERS = "trailers";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String REVIEWS = "reviews";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String IS_FAVORITE = "is_favorite";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String ID = "id";

}
