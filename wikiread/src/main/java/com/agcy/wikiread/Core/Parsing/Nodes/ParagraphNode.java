package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Core.Parsing.Text.WikiTagHandler;
import com.agcy.wikiread.Views.ParagraphView;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class ParagraphNode extends Node {
    private boolean isBold;

    public ParagraphNode(String value) {
        this.value = value;
    }

    @Override
    public Spannable getValue() {
        Spannable sp = (Spannable) Html.fromHtml(value, null, new WikiTagHandler());
        return (Spannable) Html.fromHtml(value);
    }

    @Override
    public View getView(Context context) {
        ParagraphView paragraphView = new ParagraphView(context);
        paragraphView.setText(getValue());
        if (isBold)
            paragraphView.setTypeface(null, Typeface.BOLD);
        return paragraphView;
    }

    public void setBold(boolean bold) {
        this.isBold = bold;
    }
}
