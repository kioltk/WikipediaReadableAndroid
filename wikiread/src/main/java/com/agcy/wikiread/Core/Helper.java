package com.agcy.wikiread.Core;

import android.app.Application;

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
