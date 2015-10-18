package com.example.ahmed.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.rest.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
    inspired by http://javatechig.com/android/download-and-display-image-in-android-gridview
 */
public class GridViewAdapter extends ArrayAdapter<Movie> {

    private final Context mContext;
    private final int layoutResourceId;
    private ArrayList<Movie> mGridData = new ArrayList<>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Movie> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Movie> mGridData) {
        this.mGridData = mGridData;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageView imageView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            imageView = (ImageView) row.findViewById(R.id.movie_tile);
            row.setTag(imageView);
        } else {
            imageView = (ImageView) row.getTag();
        }
        Picasso.with(mContext).load(this.mGridData.get(position).getImgUrl())
                .into(imageView);
        return row;
    }
}