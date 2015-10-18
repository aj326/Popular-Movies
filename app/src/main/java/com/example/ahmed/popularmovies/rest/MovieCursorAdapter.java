package com.example.ahmed.popularmovies.rest;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.schematic.MovieColumns;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 10/18/15.
 */



public class MovieCursorAdapter extends CursorRecyclerViewAdapter<MovieCursorAdapter.ViewHolder>
{
    Context mContext;
    ViewHolder mVh;
    public MovieCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
             {
        public ImageView mImageview;
        public ViewHolder(View view){
            super(view);
            mImageview = (ImageView) view.findViewById(R.id.movie_tile);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_tiles, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        Log.d("POSTER URL: ", cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER)));
        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER)))
                .into(viewHolder.mImageview);
    }





}
