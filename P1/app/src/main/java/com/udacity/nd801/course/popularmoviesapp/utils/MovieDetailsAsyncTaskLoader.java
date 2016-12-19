package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by dnbhatia on 12/19/2016.
 */

public class MovieDetailsAsyncTaskLoader extends AsyncTaskLoader<MovieDetails> {

    private static final String LOG_TAG = MovieDetailsAsyncTaskLoader.class.getName();
    private String mTrailerUrl;
    private String mReviewsUrl;
    private MovieDetails data;

    public MovieDetailsAsyncTaskLoader(Context context, String trailerUrl,String reviewsUrl) {
        super(context);
        mTrailerUrl=trailerUrl;
        mReviewsUrl=reviewsUrl;
    }

    @Override
    public MovieDetails loadInBackground() {
        Log.d(LOG_TAG, "Movies URL=" + mTrailerUrl);
        if (mTrailerUrl == null || mReviewsUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        MovieDetails movieDetails = DataUtils.extractFeatureTrailerFromJson(DataUtils.fetchMoviesData(mTrailerUrl),DataUtils.fetchMoviesData(mReviewsUrl));
        if (movieDetails != null)
            Log.d(LOG_TAG, movieDetails.toString());
        return movieDetails;
    }

    @Override
    public void deliverResult(MovieDetails newData) {
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



}
