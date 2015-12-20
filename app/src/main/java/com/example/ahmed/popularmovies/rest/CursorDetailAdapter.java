package com.example.ahmed.popularmovies.rest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
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
    Cursor mCursor;
    CheckBox mStar;
    public CursorDetailAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
        mCursor = cursor;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_header, parent, false);
        mCursor = cursor;
        mContext = context;
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mStar = (CheckBox) view.findViewById(R.id.star);
    }


    }


