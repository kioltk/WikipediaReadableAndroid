package com.agcy.wikiread.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final List<LangLink> langs;
    private final String title;

    public LangSwitcherAdapter(Context context, List<LangLink> langs){
        super();
        this.context = context;
        if(langs!=null)
            this.langs = langs;
        else
            this.langs = new ArrayList<LangLink>();
        this.title = null;
    }
    @Override
    public int getCount() {

        return langs.size();
    }

    @Override
    public Object getItem(int position) {
        return  langs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View langView = inflater.inflate(R.layout.list_item_drawer, parent, false);

        final LangLink item = (LangLink) getItem(position);

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
