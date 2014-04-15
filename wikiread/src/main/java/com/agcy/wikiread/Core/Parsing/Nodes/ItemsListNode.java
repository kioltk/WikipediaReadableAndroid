package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class ItemsListNode extends Node{
    public ArrayList<ListItemNode> nodes;
    public ItemsListNode( ArrayList<ListItemNode> nodes){

        this.nodes = nodes;
    }

    @Override
    public View getView(Context context){
        // ; definition list
        // : definition item
        // * asterisk
        // # number
        LinearLayout list = new LinearLayout(context);
        list.setOrientation(LinearLayout.VERTICAL);

        for(Node node:nodes){



            list.addView(node.getView(context));

        }

        return list;
    }

}
