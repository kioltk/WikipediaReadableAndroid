package com.agcy.wikiread.Views;

import android.content.Context;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by kiolt_000 on 03.03.14.
 */
public class ParagraphView extends TextView {
    public ParagraphView(Context context) {
        super(context);

        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
        setLinkTextColor(0xff33b5e5);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        setLineSpacing(10,1f);
    }
    public void setText(Spannable spannable){
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            spannable.setSpan(span, start, end, 0);
        }

        super.setText(spannable);
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
