package com.udacity.nd801.course.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.nd801.course.popularmoviesapp.utils.DetailsActivity;
import com.udacity.nd801.course.popularmoviesapp.utils.MovieAdapter;
import com.udacity.nd801.course.popularmoviesapp.utils.MovieContract;
import com.udacity.nd801.course.popularmoviesapp.utils.Movies;
import com.udacity.nd801.course.popularmoviesapp.utils.MoviesAsyncTaskLoader;
import java.util.ArrayList;
import java.util.List;

/*
@author: dnbhatia
@description: This main activity class is used for the first screen it controls the list of movies displayed to users
In this class we use TMDB API to fetch movies. To run this code you will need to generate API_KEY for TMDB and replace with
API_KEY_HERE in this file.
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movies>> {

    private static final String MOVIE_DB_REQUEST_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "API_KEY_HERE";
    private static final int MOVIE_LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int LOAD_MORE_MOVIE_ID = 2;
    private static LoaderManager movieLoaderManager;
    private static int pageId = 1;
    private static List<Movies> movieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mEmptyStateTextView;
    private Button checkConnectivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        movieList = new ArrayList<>();
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        movieLoaderManager = getLoaderManager();
        checkConnectivityButton = (Button)findViewById(R.id.check_connectivity_button);

        if (!checkConnectivity()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            checkConnectivityButton.setVisibility(View.VISIBLE);
            checkConnectivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadMore();
                }
            });
            mEmptyStateTextView.setText(R.string.no_internet_text);
        } else {
            //resetting pageId to 1
            pageId = 1;
            //Initialize Loader
            movieLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }
        //Initialize View Layout
        initLayout();

    }

    private void initLayout() {
        //Initializing Recycler View Here
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(this);
        mAdapter.setLoadMoreListener(new MovieAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();// a method which requests remote data
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMore() {
        if (!checkConnectivity()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            checkConnectivityButton.setVisibility(View.VISIBLE);
            checkConnectivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadMore();
                }
            });
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_text), Toast.LENGTH_SHORT).show();
        } else {
            movieLoaderManager.restartLoader(LOAD_MORE_MOVIE_ID, null, this);
        }
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Log.v(MainActivity.class.getName(), "3" + sortOrder);
        Uri baseUri = Uri.parse(MOVIE_DB_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (sortOrder.equals("popularity")) {
            uriBuilder.appendPath("movie");
            uriBuilder.appendPath("popular");
        } else if (sortOrder.equals("topRated")) {
            uriBuilder.appendPath("movie");
            uriBuilder.appendPath("top_rated");
        }
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.appendQueryParameter("page", String.valueOf(pageId));
        Log.v(MainActivity.class.getName(), uriBuilder.toString());
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return new MoviesAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> movies) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieList == null)
            movieList = new ArrayList<>();
        if (movies != null) {
            checkConnectivityButton.setVisibility(View.INVISIBLE);
            mEmptyStateTextView.setText(null);
            intersectList(movies, movieList);
        } else if(movieList==null){
            mEmptyStateTextView.setText(R.string.no_result_text);
        }
        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                mAdapter.setMovieData(movies);
                if (null == movies) {
                    Log.v(LOG_TAG, "No movies");
                } else {
                    showMovieDataView();
                }
                break;
            case LOAD_MORE_MOVIE_ID:
                if (null != movies) {
                    mAdapter.setMovieData(movieList);
                    mAdapter.notifyDataChanged();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {

    }

    private void showMovieDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intersectList(List<Movies> movieList1,List<Movies> movieList2){
        /*Log.v(LOG_TAG,"intersectList"+movieList2.size());*/
        int tempListSize=movieList2.size();
        for(Movies movie:movieList1){
            boolean movieFlag=false;
            for(Movies movieMain:movieList2){
                if(movie.getOriginalTitle() .equals(movieMain.getOriginalTitle())){
                    movieFlag=true;
                    break;
                }
            }
            if(movieFlag==false){
                movieList2.add(movie);
            }
        }
        if(tempListSize!=movieList2.size()){
            pageId=pageId+1;
            Log.v(LOG_TAG,pageId+"."+tempListSize+"."+movieList2.size());
        } else{
            Toast.makeText(getApplicationContext(), "Duh! we don't have any more movies for you, please visit back we are getting ready for you!! Happy Cinema", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public void onClick(String position) {
        Log.v(LOG_TAG,"Clicking on item");
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Bundle b = new Bundle();
        b.putParcelable(MovieContract.getMovieObjectString(), movieList.get(Integer.parseInt(position)));
        intentToStartDetailActivity.putExtras(b);
        startActivity(intentToStartDetailActivity);
    }

    public static void setPageId(int pageId) {
        MainActivity.pageId = pageId;
    }
}
