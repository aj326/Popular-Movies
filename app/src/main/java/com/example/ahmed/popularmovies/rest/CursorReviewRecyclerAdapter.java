package com.example.ahmed.popularmovies.rest;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.data.MovieContract;

/**
 * Created by ahmed on 10/18/15.
 */



public class CursorReviewRecyclerAdapter
        extends CursorRecyclerViewAdapter<CursorReviewRecyclerAdapter.ViewHolder>
{
    Context mContext;
    ViewHolder mVh;
    public CursorReviewRecyclerAdapter(Context context, Cursor cursor){
        super(context, cursor);
        Log.d("supering adapter","?");
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        Log.d("oncreateviewholder","?");

        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        Log.d("Binding",cursor.toString());
//        Log.d("POSTER URL: ", cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
//        Log.d("MOVIE ID, name", (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN__ID))) + " " + (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE))));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(
                MovieContract.ReviewEntry.COLUMN_AUTHOR));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(
                MovieContract.ReviewEntry.COLUMN_CONTENT));
        viewHolder.mAuthor.setText(author);
        viewHolder.mContent.setText(content);
        Log.d("adapter",author);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthor;
        public TextView mContent;
        public ViewHolder(View view) {
            super(view);
            mAuthor = (TextView) view.findViewById(R.id.list_item_review_author);
            Log.d("viewholder", mAuthor.getText().toString());
            mContent = (TextView) view.findViewById(R.id.list_item_review_content);

        }

    }
    }

