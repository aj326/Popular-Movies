package com.example.ahmed.popularmovies.rest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ahmed.popularmovies.R;

/**
 * Created by ahmed on 10/18/15.
 */



public class CursorDetailAdapter extends CursorAdapter
{


    Context mContext;
    Cursor mCurser;
    CheckBox mStar;
    public CursorDetailAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
        mCurser = cursor;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail, parent, false);
        mCurser = cursor;
        mContext = context;
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mStar = (CheckBox) view.findViewById(R.id.star);
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView mImageview;
        public CheckBox mStar;
        public ViewHolder(View view) {
            super(view);
            mStar = (CheckBox) view.findViewById(R.id.star);
//            isFav = (CheckBox) view.findViewById(R.id.star);

        }

    }
    }

