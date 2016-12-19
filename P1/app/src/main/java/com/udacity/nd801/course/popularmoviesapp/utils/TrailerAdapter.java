package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
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
    private final TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler;
    private List<Trailers> mTrailerData;

    public TrailerAdapter(TrailerAdapter.TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

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
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        Trailers trailer = mTrailerData.get(position);
        holder.mMovieTrailerView.setText(trailer.getTrailerName());
        Log.v(TAG,"..............."+trailer.getTrailerName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailer);
    }

    public void setTrailerData(List<Trailers> movieData) {
        mTrailerData = movieData;
        notifyDataSetChanged();
    }
}
