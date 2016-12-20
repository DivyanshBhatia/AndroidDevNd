package com.udacity.nd801.course.popularmoviesapp.utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.nd801.course.popularmoviesapp.R;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by dnbhatia on 12/17/2016.
 * This activity comes in picture once a user clicks on a movie poster
 * It displays the details of selected movie
 */

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails>{
    private static final String LOG_TAG= DetailsActivity.class.getName();
    private static final int TRAILER_LOADER_ID = 3;
    private ImageView movie_image_view;
    private TextView movie_original_title;
    private TextView movie_release_date;
    private TextView movie_vote_rating;
    private TextView movie_plot_synopsis;
    private Movies mMovieData;
    private MovieDetails movieDetails;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private static LoaderManager trailerLoaderManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mMovieData = b.getParcelable(MovieContract.getMovieObjectString());
            Log.v(LOG_TAG,mMovieData.toString());
        }
        initLayout();
        if(checkConnectivity()) {
            trailerLoaderManager = getLoaderManager();
            trailerLoaderManager.initLoader(TRAILER_LOADER_ID, null, this);
        } else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet_text),Toast.LENGTH_SHORT).show();
        }
    }

    /*
    The initLayout method is used to initialize Layouts
     */
    private void initLayout(){
        String posterPath=mMovieData.getMoviePosterUrl();
        //Trailer Recycle view initialize
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mTrailerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerAdapter = new TrailerAdapter();

        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        //Reviews Recycle view initialize
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mReviewRecyclerView.setHasFixedSize(true);
        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewAdapter=new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        //Initialize Detail Layout
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
        String[] movieTitleWords=mMovieData.getOriginalTitle().split(" ");
        if(movieTitleWords.length>6){
            movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.medium_size_text));
        } else if(movieTitleWords.length>3){
            movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.heading_long_text_size));
        } else{
            movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.heading_text_size));
        }
        movie_original_title.setText(mMovieData.getOriginalTitle());
        movie_plot_synopsis.setText(mMovieData.getPlotSynopsis());
        movie_release_date.setText(mMovieData.getReleaseDate());
        movie_vote_rating.setText(mMovieData.getUserRating().toString());
    }

    private String generateTrailerDataUrls(Uri baseUri) {
        String trailerUrl = null;
        String movieId = mMovieData.getMovieId();
        baseUri = Uri.parse(MovieContract.getBaseMovieUrl());
        Uri.Builder trailerUriBuilder = baseUri.buildUpon();
        trailerUriBuilder.appendPath(movieId);
        trailerUriBuilder.appendPath("videos");
        trailerUriBuilder.appendQueryParameter("api_key", MovieContract.getApiKey());
        trailerUrl = trailerUriBuilder.toString();
        return trailerUrl;
    }
    private String generateReviewsDataUrls(Uri baseUri) {
        String reviewsUrl = null;
        String movieId = mMovieData.getMovieId();
        Uri.Builder reviewsUriBuilder = baseUri.buildUpon();
        reviewsUriBuilder.appendPath(movieId);
        reviewsUriBuilder.appendPath("reviews");
        reviewsUriBuilder.appendQueryParameter("api_key", MovieContract.getApiKey());
        reviewsUrl=reviewsUriBuilder.toString();
        return reviewsUrl;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<MovieDetails> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(MovieContract.getBaseMovieUrl());
        String trailerUrl=generateTrailerDataUrls(baseUri);
        String reviewsUrl=generateReviewsDataUrls(baseUri);

        return new MovieDetailsAsyncTaskLoader(this, trailerUrl, reviewsUrl);
    }

    @Override
    public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails movieDetails) {
        mTrailerAdapter.setTrailerData(movieDetails.getmTrailers());
        mReviewAdapter.setReviewData(movieDetails.getmReviews());
        this.movieDetails=movieDetails;
        TextView video_section_label_view=((TextView) findViewById(R.id.movie_video_section_label));
        TextView review_section_label_view=((TextView) findViewById(R.id.movie_review_section_label));
        if(movieDetails.getmTrailers().size()==0) {
            video_section_label_view.setVisibility(View.GONE);
        } else{
            video_section_label_view.setVisibility(View.VISIBLE);
        }

        if(movieDetails.getmReviews().size()==0) {
            review_section_label_view.setVisibility(View.GONE);
        } else{
            review_section_label_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieDetails> loader) {

    }

    public boolean checkConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
