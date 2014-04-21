package com.agcy.wikiread.Core;

import android.app.Application;

import com.agcy.wikiread.R;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import java.util.HashMap;

/**
 * Created by kiolt_000 on 18.03.14.
 */
public class Helper {
    static Application app;
    public static void execute(Application app){
        Helper.app = app;
    }
    public static Boolean isTest(){
        return false;
    }

}
