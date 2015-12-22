package com.example.ahmed.popularmovies.adapters;

import android.content.ContentResolver;
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
import com.squareup.picasso.Picasso;

import com.example.ahmed.popularmovies.utils.Constants;

/**
 * Created by ahmed on 10/18/15.
 */



public class CursorMovieAdapter extends CursorRecyclerViewAdapter<CursorMovieAdapter.ViewHolder>
{
    public interface Callback{
         void onItemSelected(Uri movieUri);
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
        Log.d("oncreateviewholder","?");

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor){
//        DatabaseUtils.dumpCursor(cursor);
//        Log.d("POSTER URL: ", cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
//        Log.d("MOVIE ID, name", (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN__ID))) + " " + (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE))));
        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)))
                .into(viewHolder.mImageview);
        //Is there a cleaner way than to set a tag here<==================================
        final long _id = cursor.getLong(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        viewHolder.mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("onClick,poster", "what's happening?");
                ((Callback) mContext)
                        .onItemSelected(MovieContract.MovieEntry.buildMovieUri(_id));
//                Intent intent = new Intent(v.getContext(), DetailActivity.class);
//                //=======================> and get it here?
//                intent.setData(MovieContract.MovieEntry.buildMovieUri(_id));
//                v.getContext().startActivity(intent);


            }
        });
        final ContentResolver contentResolver = mContext.getContentResolver();
        viewHolder.isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE,
                           ((CheckBox) view).isChecked());
                Log.d("uri",MovieContract.MovieEntry.buildMovieUri(_id).toString());
                contentResolver.update(MovieContract.MovieEntry.buildMovieUri(_id), values,
                                       "movie_id=?",
                                       new String[]{
                                               _id+""});
            }
        });
        viewHolder.isFav.setChecked(
                (cursor.getInt(Constants.DETAIL_COLUMNS.IS_FAVORITE.ordinal()) == 1));

//        viewHolder.isFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("onClick: ", ("" + ((CheckBox) v).isChecked()));
//                if (!((CheckBox) v).isChecked()) {
//                    Log.d("isChecked: ", ("" + ((CheckBox) v).isChecked()));
//                    ((CheckBox) v).setChecked(false);
//                    Log.d("isChecked: ", ("" + ((CheckBox) v).isChecked()));
//
//                    ((CheckBox) v).setButtonDrawable(android.R.drawable.btn_star_big_off);
//                    ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(MovieContract.MovieEntry.buildMovieUri(_id));
//                    builder.withValue(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 0);
//                } else {
//                    Log.d("!isChecked: ", ("" + ((CheckBox) v).isChecked()));
//                    ((CheckBox) v).setChecked(true);
//                    ((CheckBox) v).setButtonDrawable(android.R.drawable.btn_star_big_on);
//                    ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(MovieContract.MovieEntry.buildMovieUri(_id));
//                    builder.withValue(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 1);
//                }
//
//
//            }
//        });
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

