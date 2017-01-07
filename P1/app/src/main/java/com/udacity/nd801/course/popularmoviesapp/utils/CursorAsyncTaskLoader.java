package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.udacity.nd801.course.popularmoviesapp.DatabaseUtils.FavoritesReaderContract;

/**
 * Created by dnbhatia on 12/24/2016.
 */

public class CursorAsyncTaskLoader extends CursorLoader {

    private static final String LOG_TAG=CursorAsyncTaskLoader.class.getName();
    Cursor mCursorData=null;
    Context mContext;

    public CursorAsyncTaskLoader(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onStartLoading() {
        if (mCursorData != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mCursorData);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        return mContext.getContentResolver().query(FavoritesReaderContract.FavoritesEntry.CONTENT_URI,
                null,
                FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE+"=?",
                new String[]{"1"},
                FavoritesReaderContract.FavoritesEntry._ID);
    }

    @Override
    public void deliverResult(Cursor data) {
        mCursorData = data;
        super.deliverResult(data);
    }
}
