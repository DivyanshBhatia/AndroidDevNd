package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by dnbhatia on 12/16/2016.
 */

public class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movies>> {
    private static final String LOG_TAG = MoviesAsyncTaskLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;
    private List<Movies> data;

    public MoviesAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl=url;
    }

    @Override
    public void deliverResult(List<Movies> newData) {
        Log.d(LOG_TAG, "ON_Start_Loading deliverResult to be called");
        if (isReset()) {
            // a query came in while the loader is stopped
            return;
        }
        this.data = newData;
        super.deliverResult(data);
    }


    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "ON/_Start_Loading");
        if (data != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(data);
        }
        Log.d(LOG_TAG, "ON_Start_Loading take content change to be called");
        if (takeContentChanged() || data == null) {
            Log.d(LOG_TAG, "ON_Start_Loading take content change called");
            forceLoad();
        }
    }

    @Override
    public List<Movies> loadInBackground() {
        Log.d(LOG_TAG, "Movies URL=" + mUrl);
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<Movies> movies = DataUtils.fetchMoviesData(mUrl);
        if (movies != null)
            Log.d(LOG_TAG, movies.toString());
        return movies;
    }
}

