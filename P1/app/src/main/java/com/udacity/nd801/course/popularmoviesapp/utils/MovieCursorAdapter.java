package com.udacity.nd801.course.popularmoviesapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.nd801.course.popularmoviesapp.DatabaseUtils.FavoritesReaderContract;
import com.udacity.nd801.course.popularmoviesapp.R;

/**
 * Created by dnbhatia on 12/24/2016.
 */

//Here we are using same view holder as declared in movieAdapter since it will be following a same structure
public class MovieCursorAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final MovieAdapter.MovieAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    public MovieCursorAdapter(MovieAdapter.MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder;
        viewHolder = new MovieViewHolder(view,mClickHandler);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry._ID);
        int posterIndex = mCursor.getColumnIndex(FavoritesReaderContract.FavoritesEntry.COLUMN_MOVIE_POSTER);
        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(idIndex);
        byte[] imageArr=mCursor.getBlob(posterIndex);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageArr , 0, imageArr.length);
        //Set values
        holder.itemView.setTag(id);
        holder.mMoviePosterImageView.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
