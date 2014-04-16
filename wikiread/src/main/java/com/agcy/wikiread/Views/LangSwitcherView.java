package com.agcy.wikiread.Views;

import android.content.Context;
import android.widget.ListView;

import com.agcy.wikiread.Adapters.LangSelectorAdapter;
import com.agcy.wikiread.Adapters.LangSwitcherAdapter;
import com.agcy.wikiread.Models.LangLink;

import java.util.List;

/**
 * Created by kiolt_000 on 15-Apr-14.
 */
public class LangSwitcherView extends ListView {

    public LangSwitcherView(Context context, List<LangLink> langlinks,String currentLang) {
        super(context);
        setAdapter(new LangSwitcherAdapter(context, langlinks));
    }
    public LangSwitcherView(Context context, List<LangLink> langlinks) {
        super(context);
        setAdapter(new LangSwitcherAdapter(context, langlinks));
    }
    public LangSwitcherView(Context context){
        super(context);
        setAdapter(new LangSelectorAdapter(context));
    }

}
