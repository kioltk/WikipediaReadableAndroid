package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;

import com.agcy.wikiread.Views.BlockquotedView;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class BlockquotedNode extends Node{
    public BlockquotedNode(String content){
        this.value = content;
    }

    @Override
    public View getView(Context context) {
        BlockquotedView textView =  new BlockquotedView(context);
        textView.setText(value);
        return textView;
    }
}
