package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.nd801.course.popularmoviesapp.R;

import java.util.List;

/**
 * Created by dnbhatia on 12/20/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    private List<Reviews> mReviewData;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewAdapter.ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        final Reviews review = mReviewData.get(position);
        if(review.getContent().trim().length()>3) {

            holder.mMovieReviewAuthorTextView.setText(review.getAuthor());
            String reviewContent=(review.getContent().substring(0,Math.min(review.getContent().length(),200))+"...").trim().replaceAll("(\r\n|\r|\n)"," ");
            holder.mMovieReviewContentTextView.setText(reviewContent);
            if((review.getContent().length()) >= 200){
                holder.mMovieReviewReadMoreTextView.setVisibility(View.VISIBLE);
            } else{
                holder.mMovieReviewReadMoreTextView.setVisibility(View.GONE);
            }
            holder.mMovieReviewReadMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri baseUri=Uri.parse(review.getReviewUrl());
                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    holder.mMovieReviewReadMoreTextView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uriBuilder.build()));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        public final TextView mMovieReviewAuthorTextView;
        public final TextView mMovieReviewContentTextView;
        public final TextView mMovieReviewReadMoreTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mMovieReviewAuthorTextView=(TextView)itemView.findViewById(R.id.review_author_id);
            mMovieReviewContentTextView=(TextView)itemView.findViewById(R.id.review_content_id);
            mMovieReviewReadMoreTextView=(TextView)itemView.findViewById(R.id.review_read_more_id);
        }
    }

    public void setReviewData(List<Reviews> movieReviewData) {
        mReviewData = movieReviewData;
        notifyDataSetChanged();
    }
}
