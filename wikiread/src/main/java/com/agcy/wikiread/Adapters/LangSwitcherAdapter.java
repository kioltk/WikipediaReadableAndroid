package com.agcy.wikiread.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.agcy.wikiread.Models.LangLink;
import com.agcy.wikiread.R;
import com.agcy.wikiread.Views.LangSwitcherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiolt_000 on 15-Apr-14.
 */
public class LangSwitcherAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Item> items;
    private OnClickListener onClickListener;

    public LangSwitcherAdapter(Context context, List<LangLink> langs, List<LangLink> favoriteLangs, final String currentLang){
        super();
        this.context = context;
        items = new ArrayList<Item>();

        if(currentLang!=null){
            items.add(new Header("Current"));
            items.add(new Lang(new LangLink(){{ lang = currentLang; }},false));
        }
        if(favoriteLangs!=null){
            items.add(new Header("Favorite"));
            for(LangLink langLink:favoriteLangs){
                items.add(new Lang(langLink, true));
            }
        }
        if(langs!=null){
            items.add(new Header("Available"));
            for(LangLink langLink:langs){
                items.add(new Lang(langLink, true));
            }
        }
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        final Item item = (Item) getItem(position);
        View view = item.getView(inflater, parent);
        if(item.clickable)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null)
                        onClickListener.onClick((LangLink) item.content);
                }
            });
        return view;
    }

    public void setOnClickListener(OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public abstract static class OnClickListener{
        public abstract void onClick(Object object);
    }

    private abstract class Item {
        Object content;
        Boolean clickable = false;
        public Item(Object content){
            this.content = content;
        }
        public abstract int getViewType();
        public View getView(LayoutInflater inflater, ViewGroup parent){

            return inflater.inflate(getViewType(), parent, false);
        }
    }
    private class Header extends Item{

        public Header(String content) {
            super(content);
        }
        @Override
        public int getViewType(){
            return R.layout.list_item_header_drawer;
        }
        @Override
        public View getView(LayoutInflater inflater,ViewGroup parent) {
            TextView headerView = (TextView) super.getView(inflater, parent);
            headerView.setText((String)content);
            return headerView;
        }
    }
    private class Lang extends Item {
        public Lang(LangLink content, Boolean clickable){
            super(content);
            this.clickable = clickable;
        }
        @Override
        public int getViewType(){
            return R.layout.list_item_drawer;
        }
        @Override
        public View getView(LayoutInflater inflater,ViewGroup parent) {
            View langView = super.getView(inflater, parent);
            final LangLink item = (LangLink) content;

            String[] codes = context.getResources().getStringArray(R.array.language_codes);
            String[] titles = context.getResources().getStringArray(R.array.language_names);
            TypedArray flags = context.getResources().obtainTypedArray(R.array.language_flag);

            String title = item.lang;
            Drawable flag = context.getResources().getDrawable(R.drawable.globe);
            int i;
            for (i = 0; i < codes.length; i++) {
                if (codes[i].equals(item.lang)) {
                    title = titles[i];
                    flag = flags.getDrawable(i);
                    break;
                }
            }


            ((TextView) langView.findViewById(R.id.title)).setText(title);
            try {
                ((ImageView) langView.findViewById(R.id.img)).setImageDrawable(flag);
            } catch (Exception exp) {

            }
            return langView;
        }
    }
}
