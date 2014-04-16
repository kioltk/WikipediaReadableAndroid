package com.agcy.wikiread.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agcy.wikiread.Models.LangLink;
import com.agcy.wikiread.R;

import java.util.List;

/**
 * Created by kiolt_000 on 16-Apr-14.
 */
public class LangSelectorAdapter extends BaseAdapter {

    private final Context context;
    private final LangLink[] langs;
    public LangSelectorAdapter(Context context){
        super();
        this.context = context;

        final String[] langStrings = context.getResources().getStringArray(R.array.language_codes);
        TypedArray flags = context.getResources().obtainTypedArray(R.array.language_flag);

        langs = new LangLink[langStrings.length];
        for(int i = 0; i< langStrings.length ; i++){
            final int finalI = i;
            langs[i] = new LangLink(){{
                lang = langStrings[finalI];
            }};
        }
    }
    @Override
    public int getCount() {
        return context.getResources().getStringArray(R.array.language_codes).length;
    }

    @Override
    public Object getItem(int position) {
        return langs[position];
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

        String[] titles = context.getResources().getStringArray(R.array.language_names);
        TypedArray flags = context.getResources().obtainTypedArray(R.array.language_flag);

        String title = titles[position];
        Drawable flag = flags.getDrawable(position);



        ((TextView) langView.findViewById(R.id.title)).setText(title);
        ((ImageView) langView.findViewById(R.id.img)).setImageDrawable(flag);


        return langView;
    }
}
