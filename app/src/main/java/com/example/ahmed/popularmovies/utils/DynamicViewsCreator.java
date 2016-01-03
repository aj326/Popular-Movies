package com.example.ahmed.popularmovies.utils;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by ahmed on 1/3/16.
 */
public class DynamicViewsCreator {
    public static TextView createLabel(Context context,int res)
    {
        TextView textView = new TextView(context);
        textView.setText(res);
        textView.setTextAppearance(context,
                                       android.R.style.TextAppearance_Large);
        return textView;
    }
}
