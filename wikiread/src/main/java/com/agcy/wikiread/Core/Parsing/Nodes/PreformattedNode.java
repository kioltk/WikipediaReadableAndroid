package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;

import com.agcy.wikiread.Views.PreformattedView;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class PreformattedNode extends Node{
    public PreformattedNode(String content){
        this.value = content;
    }

    @Override
    public View getView(Context context) {
        PreformattedView textView =  new PreformattedView(context);
        textView.setText(value);
        return textView;
    }
}
