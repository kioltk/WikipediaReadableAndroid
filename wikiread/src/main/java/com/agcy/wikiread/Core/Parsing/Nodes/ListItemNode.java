package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kiolt_000 on 18.03.14.
 */
public class ListItemNode extends Node {

    final public static int SIMPLE = 0;
    final public static int ITEM = -1;
    final public static int DEFINITION = -2;
    final public static int TITLE = -3;
    public int type;
    public int deep;
    public Node item;
    public ListItemNode(int type, int deep, Node item){
        this.type = type;
        this.deep = deep;
        this.item = item;
    }

    @Override
    public View getView(Context context){
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.HORIZONTAL);

        View itemDivider = new View(context);
        LinearLayout.LayoutParams params;
        switch (type) {
            case SIMPLE:
                params = new LinearLayout.LayoutParams(0, 0);
                itemDivider.setLayoutParams(params);
                break;
            case TITLE:
                ((ParagraphNode)item).setBold(true);
            case DEFINITION:
                params = new LinearLayout.LayoutParams(0, 0);
                params.setMargins(deep * 15, 20, 0, 20);
                itemDivider.setLayoutParams(params);
                break;
            case ITEM:

                params = new LinearLayout.LayoutParams(15, 15);
                params.setMargins(deep * 15, 20, 20, 20);

                itemDivider.setBackgroundColor(0xff404040);
                itemDivider.setLayoutParams(params);

                break;
        }

        view.addView(itemDivider);
        view.addView(item.getView(context));

        return view;
    }
}
