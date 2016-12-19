package com.udacity.nd801.course.popularmoviesapp.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnbhatia on 12/16/2016.
 * DataUtils class is used for fetching jsonResponse of tmdb api
 */

public class DataUtils  {

    private static final String LOG_TAG = DataUtils.class.getName();
    private DataUtils() {
    }

    public static String fetchMoviesData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Return the list of {@link Movies}s
        return jsonResponse;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Movies> extractFeatureFromJson(String moviesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<Movies> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or movies).
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");

            // For each movie in the movieArray, create an {@link Movie} object
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);
                String posterPath=null;
                String snippet=null;
                String movieTitle=null;
                String movieId = null;
                String releaseDate=null;
                Double userRating=null;

                if(currentMovie.has(MovieContract.getMoviePosterPath())) {
                    posterPath = currentMovie.getString(MovieContract.getMoviePosterPath());

                }

                if(currentMovie.has(MovieContract.getMovieOverview()))
                snippet=currentMovie.getString(MovieContract.getMovieOverview());

                if(currentMovie.has(MovieContract.getMovieOriginalTitle()))
                movieTitle=currentMovie.getString(MovieContract.getMovieOriginalTitle());

                if(currentMovie.has(MovieContract.getMovieReleaseDate()))
                releaseDate=currentMovie.getString(MovieContract.getMovieReleaseDate());

                if(currentMovie.has(MovieContract.getMovieUserRating()))
                userRating = currentMovie.getDouble(MovieContract.getMovieUserRating());

                if(currentMovie.has(MovieContract.getMovieId()))
                    movieId = currentMovie.getString(MovieContract.getMovieId());

                Movies movie = new Movies(movieId,movieTitle, posterPath, snippet,userRating, releaseDate);

                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the movies JSON results", e);
        }

        // Return the list of movies
        return movies;
    }

    public static MovieDetails extractFeatureTrailerFromJson(String moviesTrailerJSON,String movieReviewJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesTrailerJSON)) {
            return null;
        }

        List<Trailers> movieTrailers=new ArrayList<>();
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(moviesTrailerJSON);
            JSONArray movieArray = baseJsonResponse.getJSONArray(MovieContract.getMovieObjectResults());
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject currentVideo = movieArray.getJSONObject(i);
                if(currentVideo.has(MovieContract.getMovieTrailerTypeKey()) && currentVideo.getString(MovieContract.getMovieTrailerTypeKey()).equalsIgnoreCase(MovieContract.getMovieTrailerTypeValue()))
                if(currentVideo.has(MovieContract.getMovieTrailerSiteKey()) && currentVideo.getString(MovieContract.getMovieTrailerSiteKey()).equalsIgnoreCase(MovieContract.getMovieTrailerSiteValue())) {
                    String trailerName=null;
                    String trailerKey=null;
                    if (currentVideo.has(MovieContract.getMovieTrailerKey())) {
                        trailerKey=currentVideo.getString(MovieContract.getMovieTrailerKey());
                    }
                    if (currentVideo.has(MovieContract.getMovieTrailerNameKey())) {
                        trailerName=currentVideo.getString(MovieContract.getMovieTrailerNameKey());
                    }
                    Trailers trailer = new Trailers(trailerKey,trailerName);
                    movieTrailers.add(trailer);
                }

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movies JSON results", e);
        }

        return new MovieDetails(movieTrailers);
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


}
