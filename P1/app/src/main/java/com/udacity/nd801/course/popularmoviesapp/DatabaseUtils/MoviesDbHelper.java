package com.udacity.nd801.course.popularmoviesapp.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.udacity.nd801.course.popularmoviesapp.DatabaseUtils.FavoritesReaderContract.FavoritesEntry;

/**
 * Created by dnbhatia on 12/22/2016.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularMovies.db" ;
    private static final int DATABASE_VERSION=1;

    public MoviesDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE="Create Table "+ FavoritesEntry.TABLE_NAME +" ("+
                FavoritesEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavoritesEntry.COLUMN_MOVIE_ID+" INTEGER NOT NULL,"+
                FavoritesEntry.COLUMN_MOVIE_NAME+" TEXT NOT NULL, "+
                FavoritesEntry.COLUMN_MOVIE_RATING+" TEXT DEFAULT 0, "+
                FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE+" TEXT, "+
                FavoritesEntry.COLUMN_MOVIE_POSTER+" BLOB, "+
                FavoritesEntry.COLUMN_IS_FAVORITE+" INTEGER DEFAULT 0,"+
                FavoritesEntry.COLUMN_MOVIE_PLOT+" TEXT"+
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+FavoritesEntry.TABLE_NAME );
        onCreate(sqLiteDatabase);
    }
}
