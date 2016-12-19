package com.udacity.nd801.course.popularmoviesapp.utils;

import java.util.List;

/**
 * Created by dnbhatia on 12/19/2016.
 */

public class MovieDetails {
    private List<Trailers> mTrailers;
    public MovieDetails(List<Trailers> trailers){
        this.mTrailers=trailers;
    }

    public List<Trailers> getmTrailers() {
        return mTrailers;
    }
}
