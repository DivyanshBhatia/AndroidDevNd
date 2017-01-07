package com.udacity.nd801.course.popularmoviesapp.utils;

import java.util.List;

/**
 * Created by dnbhatia on 12/19/2016.
 */

public class MovieDetails {
    private List<Trailers> mTrailers;
    private List<Reviews> mReviews;
    public MovieDetails(List<Trailers> trailers,List<Reviews> reviews){
        this.mTrailers=trailers;
        this.mReviews=reviews;
    }

    public List<Trailers> getmTrailers() {
        return mTrailers;
    }

    public List<Reviews> getmReviews() {
        return mReviews;
    }
}
