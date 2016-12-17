package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.nd801.course.popularmoviesapp.R;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by dnbhatia on 12/17/2016.
 * MovieAdapter is used to support viewHolder pattern and optimizing app performance
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final MovieAdapterOnClickHandler mClickHandler;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false;
    private List<Movies> mMovieData;


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (position == mMovieData.size() - 1 && !isLoading && loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
        Movies movie = mMovieData.get(position);
        Uri uri = Uri.parse(MovieContract.getBaseImageUrl());
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendPath(MovieContract.getBaseImageSettings());
        uriBuilder.appendPath(movie.getMoviePosterUrl());

        Context context = holder.mMoviePosterImageView.getContext();
        try {
            URL url = new URL(URLDecoder.decode(uriBuilder.build().toString(), "UTF-8"));
            Picasso.with(context).load(url.toString()).into(holder.mMoviePosterImageView);
        } catch (MalformedURLException e) {
            Log.d(TAG, "#" + position + ":" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "#" + position + ":" + e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Movies> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        public final ImageView mMoviePosterImageView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.id_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }
}
