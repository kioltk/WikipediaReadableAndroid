package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.view.View;

/**
* Created by kiolt_000 on 06.03.14.
*/
public class ItemsListNode extends Node{
    public ItemsListNode(String value){
        this.value = value;
    }
    @Override
    public View getView(Context context){
        return super.getView(context);//todo:parse itemslist
    }
}
