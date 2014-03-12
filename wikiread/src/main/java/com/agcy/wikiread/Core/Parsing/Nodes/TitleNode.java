package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;

import com.agcy.wikiread.Views.ExpandableTitleView;
import com.agcy.wikiread.Views.TitleView;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class TitleNode extends Node{
    int level;
    public TitleNode(String content, int level){
        this.value = content;
        this.level = level;
    }
    public boolean isExpandable(){
        return level==0;
    }
    public View getExpandableView(Context context){
        ExpandableTitleView expandableTitleView = new ExpandableTitleView(context);
        expandableTitleView.setText(value);
        return expandableTitleView;
    }
    @Override
    public View getView(Context context) {

        TitleView titleView = new TitleView(context,level);
        titleView.setText(value);
        return titleView;
    }
}
