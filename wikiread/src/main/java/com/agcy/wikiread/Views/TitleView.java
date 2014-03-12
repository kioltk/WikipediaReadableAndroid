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

/**
 * Created by kiolt_000 on 03.03.14.
 */
public class TitleView extends TextView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public TitleView(Context context, int level) {
        super(context);
        //todo: font!
        Resources r = getResources();
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
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (25 - level*1.7));
        setTypeface(null, Typeface.BOLD);
    }
}
