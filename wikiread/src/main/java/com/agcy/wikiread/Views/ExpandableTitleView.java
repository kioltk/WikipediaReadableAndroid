package com.agcy.wikiread.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 03.03.14.
 */
public class ExpandableTitleView extends TextView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ExpandableTitleView(Context context) {
        super(context);
        setTextSize(25);
        //todo: font!
        Resources r = getResources();
        setBackground(r.getDrawable(R.drawable.title));
        setPadding(0,35,0,35);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, px, 0, px);
        setLayoutParams(params);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        setTypeface(null, Typeface.BOLD);
    }
}
