package com.example.ahmed.popularmovies.utils;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import static android.R.style.TextAppearance_Large;
import static android.R.style.TextAppearance_Material_Large;

/**
 * Created by ahmed on 1/3/16.
 */
public class DynamicViewsCreator {
    public static TextView createLabel(Context context,int res)
    {
        TextView textView = new TextView(context);
        textView.setText(res);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(TextAppearance_Material_Large);
        }
        else
            textView.setTextAppearance(context, TextAppearance_Large);

        return textView;
    }
}
