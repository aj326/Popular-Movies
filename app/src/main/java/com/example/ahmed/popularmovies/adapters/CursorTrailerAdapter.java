package com.example.ahmed.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.utils.Constants;

/**
 * Created by ahmed on 10/18/15.
 */


public class CursorTrailerAdapter extends CursorAdapter {
    Context mContext;


//    do {
//
//        String index = data.getString(Constants.TRAILER_COLUMNS.NAME.ordinal());
//        String url = data.getString(Constants.TRAILER_COLUMNS.URL.ordinal());
//        count.setText(index);
//        icon.setTag(url);
//
//        icon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
//                        "http://www.youtube.com/watch?v=" + v.getTag())));
//            }
//        });
//        Log.d(LOG_TAG, "populating listview");
//        layout.addView(icon);
//        layout.addView(count);
//        mHeader.addView(layout);
//    } while (data.moveToNext());


    public CursorTrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(context,parent);
        View view =LayoutInflater.from(context).inflate(
                R.layout.layout_trailers,
                parent,
                false);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String index = cursor.getString(Constants.TRAILER_COLUMNS.NAME.ordinal());
        String url = cursor.getString(Constants.TRAILER_COLUMNS.URL.ordinal());
        viewHolder.mName.setText(index);
        viewHolder.mIcon.setTag(url);

        viewHolder.mIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://www.youtube.com/watch?v=" + v.getTag())));
            }
        });
    }


    private static class ViewHolder {
        public final ImageView mIcon;
        public final TextView mName;
        public ViewHolder(Context context, ViewGroup parent){
            mIcon = (ImageView) LayoutInflater.from(context).inflate(R.layout.trailer_icon, parent,
                                                             false).findViewById(R.id.list_item_trailer_icon);
            mName = (TextView)LayoutInflater.from(context).inflate(R.layout.trailer_name, parent,
                                                                            false).findViewById(
                    R.id.list_item_trailer_name);



        }
    }
}
