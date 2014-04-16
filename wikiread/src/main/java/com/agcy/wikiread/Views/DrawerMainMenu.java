package com.agcy.wikiread.Views;

import android.content.Context;
import android.widget.ListView;

import com.agcy.wikiread.Adapters.MainMenuAdapter;

/**
 * Created by kiolt_000 on 16-Apr-14.
 */
public class DrawerMainMenu extends ListView {


    public DrawerMainMenu(Context context) {
        super(context);
        setAdapter(new MainMenuAdapter(context));
    }

}
