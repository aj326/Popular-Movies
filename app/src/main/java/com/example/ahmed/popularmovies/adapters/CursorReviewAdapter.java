package com.example.ahmed.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.provider.MovieContract;

/**
 * Created by ahmed on 10/18/15.
 */



public class CursorReviewAdapter extends CursorAdapter
{

    public CursorReviewAdapter(Context context, Cursor cursor){
        super(context, cursor);
    }
    private Cursor mCursor;
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.list_item_review, parent, false);
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        DatabaseUtils.dumpCursor(cursor);
        TextView tvAuthor = (TextView) view.findViewById(R.id.list_item_review_author);
        TextView tvContent = (TextView) view.findViewById(R.id.list_item_review_content);
        String author = cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
        String content = cursor.getString(cursor.getColumnIndex(
                MovieContract.ReviewEntry.COLUMN_CONTENT));
        tvAuthor.setText(author);
        tvContent.setText(content);

    }

    @Override
    public Object getItem(int position) {
        return mCursor.moveToPosition(position);
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        mCursor.getLong(mCursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_MOVIE_ID));
        return super.getItemId(position);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Log.d("cursorreview","swap!");
        return super.swapCursor(newCursor);
    }
}


