package com.udacity.nd801.course.popularmoviesapp.DatabaseUtils;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dnbhatia on 12/22/2016.
 * @description: this class is the database contract for the classes that want to make database calls (CRUD operations)
 */

public class FavoritesReaderContract {

    //URI specifics
    public static final String AUTHORITY="com.udacity.nd801.course.popularmoviesapp"; //Authority as declared in Manifest file
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_TASKS = "tasks";
    public static final String ITEM_DATA = "#";

    private FavoritesReaderContract(){}

    public static class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        //Following variables define databse specifics
        public static final String TABLE_NAME="favorites";
        public static final String COLUMN_MOVIE_ID="movieId";
        public static final String COLUMN_MOVIE_NAME="name";
        public static final String COLUMN_MOVIE_POSTER="poster";
        public static final String COLUMN_MOVIE_RELEASE_DATE="releaseDate";
        public static final String COLUMN_MOVIE_RATING="rating";
        public static final String COLUMN_IS_FAVORITE="isFavorite";
        public static final String COLUMN_MOVIE_PLOT="moviePlot";
    }
}
