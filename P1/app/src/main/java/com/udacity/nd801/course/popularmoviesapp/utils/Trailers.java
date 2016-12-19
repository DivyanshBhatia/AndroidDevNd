package com.udacity.nd801.course.popularmoviesapp.utils;

/**
 * Created by dnbhatia on 12/19/2016.
 */

public class Trailers {
    private String trailerKey;
    private String trailerName;

    public Trailers(String trailerKey,String trailerName){
        this.trailerKey=trailerKey;
        this.trailerName=trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }
}
