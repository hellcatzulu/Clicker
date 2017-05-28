package com.studio.confidoworks.clicker;

/**
 * created by hellcat-zulu on 24-May-17
 */

import android.content.Context;
import android.content.SharedPreferences;

public class variables
{
    public static final String PREFS_NAME = "var"; //primitive save file name
    Context app;                                   //defined context for SharedPreferences
    //public String var;
    //public int var1

    public int clicks;
    public int cps;
    public int store1Num;
    public int store1Price;
    public int store2Num;
    public int store2Price;

    public SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
    public SharedPreferences.Editor varSaver = varReader.edit();

    public void load()
    {
        clicks = varReader.getInt("clicks", 0);
        cps = varReader.getInt("cps", 0);
        store1Num = varReader.getInt("store1Num", 0);
        store1Price = varReader.getInt("store1Price", 1);
        store2Num = varReader.getInt("store2Num", 0);
        store2Price = varReader.getInt("store2Price", 2);
    }

    public void fullSave()
    {
        varSaver.putInt("clicks", clicks);
        varSaver.putInt("cps", cps);
        varSaver.putInt("store1Num", store1Num);
        varSaver.putInt("store1Price", store1Price);
        varSaver.putInt("store2Num", store2Num);
        varSaver.putInt("store2Price", store2Price);
    }
}