package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Core.Helper;
import com.agcy.wikiread.Views.SimpleWikiView;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class SimpleWikiNode extends Node{
    public SimpleWikiNode(String value){
        this.value = value;
    }
    @Override
    public Spannable getValue(){
        //todo: tag handler!
        if(isEmpty()){
            if(Helper.isTest())
                value = "#EMPTYNODE#";
        }
        Spannable sp = (Spannable) Html.fromHtml(value, null, null);
        return (Spannable) Html.fromHtml(value);
    }
    @Override
    public View getView(Context context){
        if(isEmpty()){
            return new View(context);
        }
        SimpleWikiView simpleWikiView = new SimpleWikiView(context);
        simpleWikiView.setText(getValue());
        return simpleWikiView;
    }
    public Boolean isEmpty(){
        return value==null || this.value.isEmpty();
    }
}
