package com.udacity.nd801.course.popularmoviesapp.utils;

/**
 * Created by dnbhatia on 12/17/2016.
 * This class controls the json parameters and TMDB paths and is one source of request strings for entire code
 */

public class MovieContract {
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org";
    private static final String BASE_IMAGE_SETTINGS = "t/p/w185";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_USER_RATING = "vote_average";
    private static final String MOVIE_OBJECT_STRING = "movieObjectString";


    public static String getBaseImageUrl() {
        return BASE_IMAGE_URL;
    }

    public static String getBaseImageSettings() {
        return BASE_IMAGE_SETTINGS;
    }

    public static String getMoviePosterPath() {
        return MOVIE_POSTER_PATH;
    }

    public static String getMovieOverview() {
        return MOVIE_OVERVIEW;
    }

    public static String getMovieOriginalTitle() {
        return MOVIE_ORIGINAL_TITLE;
    }

    public static String getMovieReleaseDate() {
        return MOVIE_RELEASE_DATE;
    }

    public static String getMovieUserRating() {
        return MOVIE_USER_RATING;
    }

    public static String getMovieObjectString() {
        return MOVIE_OBJECT_STRING;
    }
}
