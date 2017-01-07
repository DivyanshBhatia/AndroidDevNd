package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.nd801.course.popularmoviesapp.R;

import java.util.List;

/**
 * Created by dnbhatia on 12/19/2016.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private List<Trailers> mTrailerData;

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerViewHolder holder, int position) {
        final Trailers trailer = mTrailerData.get(position);
        holder.mMovieTrailerView.setText(trailer.getTrailerName());
        holder.mMovieTrailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri baseUri=Uri.parse(MovieContract.getMovieYoutubeUrl());
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter("v",trailer.getTrailerKey());
                holder.mMovieTrailerView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uriBuilder.build()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        public final TextView mMovieTrailerView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public TrailerViewHolder(View itemView) {
            super(itemView);
            mMovieTrailerView = (TextView) itemView.findViewById(R.id.id_movie_trailer);
        }

    }

    public void setTrailerData(List<Trailers> movieData) {
        mTrailerData = movieData;
        notifyDataSetChanged();
    }
}
