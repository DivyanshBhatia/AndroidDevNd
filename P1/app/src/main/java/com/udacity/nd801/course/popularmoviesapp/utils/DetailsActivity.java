package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nd801.course.popularmoviesapp.R;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by dnbhatia on 12/17/2016.
 * This activity comes in picture once a user clicks on a movie poster
 * It displays the details of selected movie
 */

public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG= DetailsActivity.class.getName();
    private ImageView movie_image_view;
    private TextView movie_original_title;
    private TextView movie_release_date;
    private TextView movie_vote_rating;
    private TextView movie_plot_synopsis;
    private Movies mMovieData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mMovieData = b.getParcelable(MovieContract.getMovieObjectString());
            Log.v(LOG_TAG,mMovieData.toString());
        }
        String posterPath=mMovieData.getMoviePosterUrl();
        movie_image_view=(ImageView)findViewById(R.id.detail_image_id);
        movie_original_title=(TextView)findViewById(R.id.detail_title_value);
        movie_release_date=(TextView)findViewById(R.id.release_date_value);
        movie_plot_synopsis=(TextView)findViewById(R.id.detail_synopsis_value);
        movie_vote_rating=(TextView)findViewById(R.id.vote_rating_value);

        if(posterPath!=null){
            Uri uri=Uri.parse(MovieContract.getBaseImageUrl());
            Uri.Builder uriBuilder = uri.buildUpon();
            uriBuilder.appendPath(MovieContract.getBaseImageSettings());
            uriBuilder.appendPath(posterPath);

            Context context=movie_image_view.getContext();
            try {
                URL url = new URL(URLDecoder.decode(uriBuilder.build().toString(), "UTF-8"));
                Picasso.with(context).load(url.toString()).into(movie_image_view);
            } catch (MalformedURLException e) {
                Log.d(LOG_TAG, e.getMessage());
            } catch (UnsupportedEncodingException e) {
                Log.d(LOG_TAG,e.getMessage());
            }
        }
        movie_original_title.setText(mMovieData.getOriginalTitle());
        movie_plot_synopsis.setText(mMovieData.getPlotSynopsis());
        movie_release_date.setText(mMovieData.getReleaseDate());
        movie_vote_rating.setText(mMovieData.getUserRating().toString());
    }
}
