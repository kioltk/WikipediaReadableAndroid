package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Views.ParagraphView;

import org.xml.sax.XMLReader;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class ParagraphNode extends Node{
    public ParagraphNode(String value){
        this.value = value;
    }
    @Override
    public Spannable getValue(){
        Spannable sp = (Spannable) Html.fromHtml(value, null, new Html.TagHandler() {

            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

            }
        });
        return (Spannable) Html.fromHtml(value);
    }
    @Override
    public View getView(Context context){
        ParagraphView paragraphView = new ParagraphView(context);
        paragraphView.setText(getValue());
        return paragraphView;//todo: wikiParagraphView
    }

}
