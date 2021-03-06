package com.agcy.wikiread.Core.Parsing.Text;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;

import com.agcy.wikiread.Core.Helper;

import org.xml.sax.XMLReader;

/**
 * Created by kiolt_000 on 20.03.14.
 */
public class WikiTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.equalsIgnoreCase("ref")) {
            processProof(opening, output);
        }
    }

    private void processProof(boolean opening, Editable output) {
        int currentIndex = output.length();
        if(opening) {
            output.setSpan(new ProofSpan(), currentIndex, currentIndex, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, ProofSpan.class);
            int spanStart = output.getSpanStart(obj);

            output.removeSpan(obj);
            if(Helper.isTest()) {
                output.replace(spanStart, currentIndex, "#PROOF#");
                currentIndex = spanStart + 6;
            }else {
                output.replace(spanStart, currentIndex, "");
                currentIndex = spanStart;
            }
            if (spanStart != currentIndex) {
                output.setSpan(new ProofSpan(), spanStart, currentIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for(int i = objs.length;i>0;i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }
}
