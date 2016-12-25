package com.udacity.nd801.course.popularmoviesapp.utils;


import android.graphics.BitmapFactory;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.nd801.course.popularmoviesapp.DatabaseUtils.FavoritesReaderContract;
import com.udacity.nd801.course.popularmoviesapp.R;

import java.io.ByteArrayOutputStream;
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

public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG= DetailsActivity.class.getName();
    private static final int TRAILER_LOADER_ID = 3;
    private static final int CURSOR_LOADER_ID = 4;
    private ImageView movie_image_view;
    private TextView movie_original_title;
    private TextView movie_release_date;
    private TextView movie_vote_rating;
    private TextView movie_plot_synopsis;
    private Button movie_favorites_button;
    private String movieId;
    private Movies mMovieData;
    private MovieDetails movieDetails;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private static LoaderManager trailerLoaderManager;
    private LoaderManager.LoaderCallbacks<MovieDetails> mMovieDetailsLoader;
    private LoaderManager.LoaderCallbacks<Cursor> mCursorLoader;
    private Cursor mCursor;
    private Context context;
    private String sortOrder;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context=getApplicationContext();
        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            sortOrder=b.getString(MovieContract.getMovieSortOrderObjectString());
            if(sortOrder.equals("popularity")||sortOrder.equals("topRated")) {
                mMovieData = b.getParcelable(MovieContract.getMovieObjectString());
                movieId=mMovieData.getMovieId();
                Log.v(LOG_TAG,mMovieData.toString());
            } else if(sortOrder.equals("favorites")){
                movieId=String.valueOf(b.getInt(MovieContract.getMovieIdObjectString()));
                Log.v(LOG_TAG,movieId);
            }
        }
        mCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>(){

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CursorLoader(context,
                        FavoritesReaderContract.FavoritesEntry.CONTENT_URI.buildUpon().appendPath(movieId).build(),
                        null,
                        null,
                        null,
                        FavoritesReaderContract.FavoritesEntry._ID);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                mCursor=cursor;
                mCursor.moveToFirst();
                initLayout(sortOrder);

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID,null, mCursorLoader);
        mTrailerAdapter = new TrailerAdapter();
        mReviewAdapter=new ReviewAdapter();
        if(checkConnectivity()) {

            mMovieDetailsLoader= new LoaderManager.LoaderCallbacks<MovieDetails>() {
                @Override
                public Loader<MovieDetails> onCreateLoader(int i, Bundle bundle) {
                    Uri baseUri = Uri.parse(MovieContract.getBaseMovieUrl());
                    String trailerUrl=generateTrailerDataUrls(baseUri,movieId);
                    String reviewsUrl=generateReviewsDataUrls(baseUri,movieId);
                    return new MovieDetailsAsyncTaskLoader(context, trailerUrl, reviewsUrl);
                }

                @Override
                public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails movieDetailsData) {
                    mTrailerAdapter.setTrailerData(movieDetailsData.getmTrailers());
                    mReviewAdapter.setReviewData(movieDetailsData.getmReviews());
                    movieDetails=movieDetailsData;
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
            };
            getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, mMovieDetailsLoader);
        } else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet_text),Toast.LENGTH_SHORT).show();
        }
    }

    /*
    The initLayout method is used to initialize Layouts
     */
    private void initLayout(String sortOrder){

        //Local variables to initLayout
        String originalTitle=null;
        String plotSynopsis=null;
        String userRating=null;
        String releaseDate=null;
        int isFavorite=0;

        //Trailer Recycle view initialize
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mTrailerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        //Reviews Recycle view initialize
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mReviewRecyclerView.setHasFixedSize(true);
        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);

        mReviewRecyclerView.setAdapter(mReviewAdapter);

        //Initialize Detail Layout
        movie_image_view=(ImageView)findViewById(R.id.detail_image_id);
        movie_original_title=(TextView)findViewById(R.id.detail_title_value);
        movie_release_date=(TextView)findViewById(R.id.release_date_value);
        movie_plot_synopsis=(TextView)findViewById(R.id.detail_synopsis_value);
        movie_vote_rating=(TextView)findViewById(R.id.vote_rating_value);
        movie_favorites_button = (Button) findViewById(R.id.add_to_favorites_id);

        if(sortOrder.equals("popularity")||sortOrder.equals("topRated")) {
            originalTitle=mMovieData.getOriginalTitle();
            plotSynopsis=mMovieData.getPlotSynopsis();
            releaseDate=mMovieData.getReleaseDate();
            userRating=mMovieData.getUserRating().toString();

            //Making Picasso Call to Fetch from Network
            String posterPath=mMovieData.getMoviePosterUrl();
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

        }
        if(mCursor!=null && mCursor.moveToFirst()){
            originalTitle=mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_NAME));
            plotSynopsis=mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_PLOT));
            userRating=mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RATING));
            releaseDate=mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE));
            byte[] imageArr=mCursor.getBlob(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_POSTER));
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageArr , 0, imageArr.length);
            movie_image_view.setImageBitmap(bitmap);
            isFavorite = mCursor.getInt(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE));
        }

            if(isFavorite==0){
                movie_favorites_button.setText(getResources().getString(R.string.add_favorites));
            } else{
                movie_favorites_button.setText(getResources().getString(R.string.remove_favorites));
            }

        if(originalTitle!=null) {
            String[] movieTitleWords = originalTitle.split(" ");
            if (movieTitleWords.length > 6) {
                movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.medium_size_text));
            } else if (movieTitleWords.length > 3) {
                movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.heading_long_text_size));
            } else {
                movie_original_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.heading_text_size));
            }
        }
        movie_original_title.setText(originalTitle);
        movie_plot_synopsis.setText(plotSynopsis);
        movie_release_date.setText(releaseDate);
        movie_vote_rating.setText(userRating);
        movie_favorites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportLoaderManager().initLoader(CURSOR_LOADER_ID,null, mCursorLoader);
                if(mCursor==null || (mCursor!=null &&!mCursor.moveToFirst())) {
                    addMovieToFavoritesDb();
                } else if(mCursor.getInt(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE))==1){
                    updateMovieInFavoritesDb(0);
                } else if(mCursor.getInt(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE))==0){
                    updateMovieInFavoritesDb(1);
                }
            }
        });
    }

    private String generateTrailerDataUrls(Uri baseUri,String movieId ) {
        String trailerUrl = null;
        baseUri = Uri.parse(MovieContract.getBaseMovieUrl());
        Uri.Builder trailerUriBuilder = baseUri.buildUpon();
        trailerUriBuilder.appendPath(movieId);
        trailerUriBuilder.appendPath("videos");
        trailerUriBuilder.appendQueryParameter("api_key", MovieContract.getApiKey());
        trailerUrl = trailerUriBuilder.toString();
        return trailerUrl;
    }
    private String generateReviewsDataUrls(Uri baseUri,String movieId) {
        String reviewsUrl = null;
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


    public boolean checkConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void addMovieToFavoritesDb(){
        Log.v(LOG_TAG,"addMovieToFavoritesDb :"+mMovieData.toString());
        ContentValues contentValues=new ContentValues();
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_ID,Integer.parseInt(mMovieData.getMovieId()));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_NAME,mMovieData.getOriginalTitle());
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RATING,mMovieData.getUserRating());
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE,mMovieData.getReleaseDate());
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_PLOT,mMovieData.getPlotSynopsis());

        //Converting image in imageView to byte[]
        Bitmap bitmap = ((BitmapDrawable)movie_image_view.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap .compress(Bitmap.CompressFormat.PNG, 100, bos);

        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_POSTER,bos.toByteArray());
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE,1);
        Uri uri=getContentResolver().insert(FavoritesReaderContract.FavoritesEntry.CONTENT_URI,contentValues);
        if(uri!=null){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.movie_inserted_placeholder), Toast.LENGTH_LONG).show();
            movie_favorites_button.setText(getResources().getString(R.string.remove_favorites));
        }
    }
    private void updateMovieInFavoritesDb(int isFavorite){
        ContentValues contentValues=new ContentValues();
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_ID,mCursor.getInt(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_ID)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_NAME,mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_NAME)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RATING,mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RATING)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE,mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_PLOT,mCursor.getString(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_PLOT)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_POSTER,mCursor.getBlob(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_POSTER)));
        contentValues.put(FavoritesReaderContract.FavoritesEntry.COLUMN_IS_FAVORITE,isFavorite);
        int updatedRows=getContentResolver().update(FavoritesReaderContract.FavoritesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_ID)))).build(),contentValues,null,null);
        if(updatedRows!=0){

            if(isFavorite==0) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.movie_removed_placeholder), Toast.LENGTH_LONG).show();
                movie_favorites_button.setText(getResources().getString(R.string.add_favorites));
            } else if(isFavorite==1){
                Toast.makeText(getBaseContext(), getResources().getString(R.string.movie_inserted_placeholder), Toast.LENGTH_LONG).show();
                movie_favorites_button.setText(getResources().getString(R.string.remove_favorites));
            }
        }
    }
}
