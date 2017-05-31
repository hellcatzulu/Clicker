package com.studio.confidoworks.clicker;

import android.content.Context;
import android.content.SharedPreferences;

class variables
{
    private static final String PREFS_NAME = "var";
    Context app;

    static int clicks;
    static int cps;
    static int clicksPC;
    static int store1Num;
    static int store1Price;
    static int store2Num;
    static int store2Price;

    SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor varSaver = varReader.edit();

    void load()
    {
        clicks = varReader.getInt("clicks", 0);
        cps = varReader.getInt("cps", 0);
        clicksPC = varReader.getInt("clicksPC", 1);
        store1Num = varReader.getInt("store1Num", 0);
        store1Price = varReader.getInt("store1Price", 1);
        store2Num = varReader.getInt("store2Num", 0);
        store2Price = varReader.getInt("store2Price", 2);
    }

    void fullSave()
    {
        varSaver.putInt("clicks", clicks);
        varSaver.putInt("cps", cps);
        varSaver.putInt("clicksPC", clicksPC);
        varSaver.putInt("store1Num", store1Num);
        varSaver.putInt("store1Price", store1Price);
        varSaver.putInt("store2Num", store2Num);
        varSaver.putInt("store2Price", store2Price);
    }
}