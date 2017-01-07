package com.udacity.nd801.course.popularmoviesapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.udacity.nd801.course.popularmoviesapp.R;

class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        public final ImageView mMoviePosterImageView;
        private final MovieAdapter.MovieAdapterOnClickHandler mClickHandler;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieViewHolder(View itemView,MovieAdapter.MovieAdapterOnClickHandler clickHandler) {
            super(itemView);
            this.mClickHandler=clickHandler;
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.id_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }