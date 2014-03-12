package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class GroupNode extends Node{
    public TitleNode title;
    public ArrayList<Node> nodes;
    public GroupNode(TitleNode title){
        this.title = title;
        nodes = new ArrayList<Node>();
    }
    @Override
    public View getView(Context context) {

        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout list = new LinearLayout(context);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setVisibility(View.GONE);

        for(Node node:nodes){
            list.addView(node.getView(context));
        }
        View titleView = title.getExpandableView(context);

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.getVisibility()==View.GONE)
                    list.setVisibility(View.VISIBLE);
                else
                    list.setVisibility(View.GONE);

            }
        });

        view.addView(titleView);
        view.addView(list);
        return view;

    }
    public void add(Node node){
        nodes.add(node);
    }
}
