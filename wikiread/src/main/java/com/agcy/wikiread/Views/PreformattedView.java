package com.agcy.wikiread.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 04.03.14.
 */
public class PreformattedView extends TextView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public PreformattedView(Context context) {
        super(context);
        //todo: font!

        Resources r = getResources();
        setBackground(r.getDrawable(R.drawable.border));
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, px, 0, px);
        setPadding(20, 15, 20, 15);
        setLayoutParams(params);

        setMovementMethod(new ScrollingMovementMethod());

        setHorizontallyScrolling(true);
        setHorizontalScrollBarEnabled(true);

    }



}
