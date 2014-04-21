package com.agcy.wikiread;

import android.app.Application;

import com.agcy.wikiread.Core.Helper;

/**
 * Created by kiolt_000 on 21-Apr-14.
 */
public class WikiApplication extends Application {
    @Override
    public void onCreate()
    {

        super.onCreate();
        Helper.execute(this);

    }
}
