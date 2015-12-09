package com.example.ahmed.popularmovies.rest;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.R;
import com.example.ahmed.popularmovies.schematic.MovieColumns;
import com.example.ahmed.popularmovies.schematic.MoviesProvider;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 10/18/15.
 */



public class CursorMovieAdapter extends CursorRecyclerViewAdapter<CursorMovieAdapter.ViewHolder>
{
    public interface Callback{
        public void onItemSelected(Uri movieUri);
    }
    
    Context mContext;
    ViewHolder mVh;
    public CursorMovieAdapter(Context context, Cursor cursor){
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
//        DatabaseUtils.dumpCursor(cursor);
        Log.d("POSTER URL: ", cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER)));
        Log.d("MOVIE ID, name", (cursor.getString(cursor.getColumnIndex(MovieColumns._ID))) + " " + (cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE))));
        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER)))
                .into(viewHolder.mImageview);
        //Is there a cleaner way than to set a tag here<==================================
        final long _id = cursor.getLong(cursor.getColumnIndex(MovieColumns._ID));
        viewHolder.mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callback) mContext)
                        .onItemSelected(MoviesProvider.Movies.withId(_id)
                        );
//                Intent intent = new Intent(v.getContext(), DetailActivity.class);
//                //=======================> and get it here?
//                intent.setData(MoviesProvider.Movies.withId(_id));
//                v.getContext().startActivity(intent);


            }
        });
//        viewHolder.mStar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("onClick: ", ("" + ((CheckBox) v).isChecked()));
//                if (!((CheckBox) v).isChecked()) {
//                    Log.d("isChecked: ", ("" + ((CheckBox) v).isChecked()));
//                    ((CheckBox) v).setChecked(false);
//                    Log.d("isChecked: ", ("" + ((CheckBox) v).isChecked()));
//
//                    ((CheckBox) v).setButtonDrawable(android.R.drawable.btn_star_big_off);
//                    ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(MoviesProvider.Movies.withId(_id));
//                    builder.withValue(MovieColumns.IS_FAVORITE, 0);
//                } else {
//                    Log.d("!isChecked: ", ("" + ((CheckBox) v).isChecked()));
//                    ((CheckBox) v).setChecked(true);
//                    ((CheckBox) v).setButtonDrawable(android.R.drawable.btn_star_big_on);
//                    ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(MoviesProvider.Movies.withId(_id));
//                    builder.withValue(MovieColumns.IS_FAVORITE, 1);
//                }
//
//
//            }
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageview;
//        public CheckBox mStar;
        public ViewHolder(View view) {
            super(view);
            mImageview = (ImageView) view.findViewById(R.id.poster);
//            mStar = (CheckBox) view.findViewById(R.id.star);

        }

    }
    }

