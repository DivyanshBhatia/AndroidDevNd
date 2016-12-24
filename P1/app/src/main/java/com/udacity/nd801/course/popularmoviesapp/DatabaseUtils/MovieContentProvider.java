package com.udacity.nd801.course.popularmoviesapp.DatabaseUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.udacity.nd801.course.popularmoviesapp.utils.MovieContract;

/**
 * Created by dnbhatia on 12/24/2016.
 */

public class MovieContentProvider extends ContentProvider {

    private MoviesDbHelper mMovieDbHelper;
    public static final int MOVIE_TASKS=100;
    public static final int MOVIE_TASKS_WITH_ID=101;
    private static final UriMatcher sUriMatcher=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesReaderContract.AUTHORITY,FavoritesReaderContract.PATH_TASKS,MOVIE_TASKS);
        uriMatcher.addURI(FavoritesReaderContract.AUTHORITY,FavoritesReaderContract.PATH_TASKS+"/#",MOVIE_TASKS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        mMovieDbHelper=new MoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase moviesDb=mMovieDbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri=null;
        switch(match){
            case MOVIE_TASKS:
                long id = moviesDb.insert(FavoritesReaderContract.FavoritesEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri= ContentUris.withAppendedId(FavoritesReaderContract.FavoritesEntry.CONTENT_URI,id);
                } else{
                    throw new android.database.SQLException("Unable to insert row with "+uri);
                }
                break;

            case MOVIE_TASKS_WITH_ID:
                break;

            default:
                throw new UnsupportedOperationException("Uri not found:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
