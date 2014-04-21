package com.agcy.wikiread.Views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agcy.wikiread.Adapters.LangSelectorAdapter;
import com.agcy.wikiread.Adapters.LangSwitcherAdapter;
import com.agcy.wikiread.Models.LangLink;

import java.util.ArrayList;
import java.util.List;

public class LangSwitcherView extends ListView {
    LangSwitcherAdapter adapter;
    public LangSwitcherView(Context context, List<LangLink> langlinks, String currentLang, Boolean includeFavorite){
        super(context);


        if(includeFavorite){
            //todo:favorite selector
            //this.favoriteLangsSwitcher = new ListView(context);
        }



        this.adapter = new LangSwitcherAdapter(context,langlinks,null,currentLang);
        setAdapter(this.adapter);

    }
    public void setOnClickListener(LangSwitcherAdapter.OnClickListener listener){
        adapter.setOnClickListener(listener);
    }
}
