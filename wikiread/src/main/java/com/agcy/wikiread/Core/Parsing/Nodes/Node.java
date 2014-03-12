package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
* Created by kiolt_000 on 06.03.14.
*/ //region Nodes
public class Node {
    public String value;
    public Node(){

    }
    public Node(String content){
        this.value = content;
    }
    public Object getValue(){
        return value;
    }
    public View getView(Context context){
        if(!value.equals("")){
            TextView textView = new TextView(context);
            textView.setText(value);
            return textView;
        }
        else{
            View view = new View(context);
            view.setBackgroundColor(0xff000000);
            view.setMinimumHeight(1);
            return view;
        }
    }
}
