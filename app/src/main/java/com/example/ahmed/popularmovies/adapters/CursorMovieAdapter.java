package com.example.ahmed.popularmovies.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.provider.MovieContract;
import com.example.ahmed.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 10/18/15.
 */


public class CursorMovieAdapter extends CursorRecyclerViewAdapter<CursorMovieAdapter.ViewHolder> {
    public interface Callback {
        void onItemSelected(Uri movieUri, String movieName);
    }


    Context mContext;
    ViewHolder mVh;

    public CursorMovieAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_tiles, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor) {
        Picasso.with(mContext).load(cursor.getString(Constants.DETAIL_COLUMNS.POSTER.ordinal()))
                .into(viewHolder.mImageview);
        final long movieId = cursor.getLong(
                Constants.DETAIL_COLUMNS.MOVIE_ID.ordinal());
        final String name = cursor.getString(
                Constants.DETAIL_COLUMNS.TITLE.ordinal());
        viewHolder.mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callback) mContext)
                        .onItemSelected(MovieContract.MovieEntry.buildMovieUri(movieId), name);
            }
        });
        viewHolder.isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE,
                           ((CheckBox) view).isChecked());
                Log.d("uri", MovieContract.MovieEntry.buildMovieUri(movieId).toString());
                mContext.getContentResolver().update(
                        MovieContract.MovieEntry.buildMovieUri(movieId), values,
                        "movie_id=?",
                        new String[]{
                                movieId + ""});
            }
        });
        viewHolder.isFav.setChecked(
                (cursor.getInt(Constants.DETAIL_COLUMNS.IS_FAVORITE.ordinal()) == 1));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageview;
        public CheckBox isFav;

        public ViewHolder(View view) {
            super(view);
            mImageview = (ImageView) view.findViewById(R.id.poster);
            isFav = (CheckBox) view.findViewById(R.id.star);

        }

    }
}

