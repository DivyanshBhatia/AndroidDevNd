package com.udacity.nd801.course.popularmoviesapp.utils;

/**
 * Created by dnbhatia on 12/17/2016.
 * This class controls the json parameters and TMDB paths and is one source of request strings for entire code
 */

public class MovieContract {
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org";
    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY = "API_KEY_HERE";
    private static final String BASE_IMAGE_SETTINGS = "t/p/w185";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_USER_RATING = "vote_average";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_OBJECT_STRING = "movieObjectString";
    private static final String MOVIE_OBJECT_RESULTS = "results";

    //Trailer Section
    private static final String MOVIE_TRAILER_KEY = "key";
    private static final String MOVIE_TRAILER_TYPE_KEY = "type";
    private static final String MOVIE_TRAILER_TYPE_VALUE = "Trailer";
    private static final String MOVIE_TRAILER_SITE_KEY = "site";
    private static final String MOVIE_TRAILER_SITE_VALUE = "YouTube";
    private static final String MOVIE_TRAILER_NAME_KEY = "name";
    private static final String MOVIE_YOUTUBE_URL = "https://www.youtube.com/watch";

    //Review Section
    private static final String MOVIE_REVIEW_AUTHOR_KEY = "author";
    private static final String MOVIE_REVIEW_CONTENT_KEY = "content";
    private static final String MOVIE_REVIEW_URL_KEY = "url";


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

    public static String getMovieId() {
        return MOVIE_ID;
    }

    public static String getBaseMovieUrl() {
        return BASE_MOVIE_URL;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getMovieObjectResults() {
        return MOVIE_OBJECT_RESULTS;
    }

    public static String getMovieTrailerKey() {
        return MOVIE_TRAILER_KEY;
    }

    public static String getMovieTrailerNameKey() {
        return MOVIE_TRAILER_NAME_KEY;
    }

    public static String getMovieTrailerSiteValue() {
        return MOVIE_TRAILER_SITE_VALUE;
    }

    public static String getMovieTrailerTypeValue() {
        return MOVIE_TRAILER_TYPE_VALUE;
    }

    public static String getMovieTrailerTypeKey() {
        return MOVIE_TRAILER_TYPE_KEY;
    }

    public static String getMovieTrailerSiteKey() {
        return MOVIE_TRAILER_SITE_KEY;
    }

    public static String getMovieObjectString() {
        return MOVIE_OBJECT_STRING;
    }

    public static String getMovieYoutubeUrl() {
        return MOVIE_YOUTUBE_URL;
    }

    public static String getMovieReviewAuthorKey() {
        return MOVIE_REVIEW_AUTHOR_KEY;
    }

    public static String getMovieReviewContentKey() {
        return MOVIE_REVIEW_CONTENT_KEY;
    }

    public static String getMovieReviewUrlKey() {
        return MOVIE_REVIEW_URL_KEY;
    }
}
