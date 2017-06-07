package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class main extends Activity
{
    public static final String PREFS_NAME="save";
    public static boolean run;
    Context app = this;
    //TextView clickView = (TextView)findViewById(R.id.clickView);
    //TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
    //TextView creditsView  = (TextView)findViewById(R.id.creditsView);
    //SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
    //SharedPreferences.Editor varSaver = varReader.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        run = true;
        load();
        Intent activeClick = new Intent(this, ClickService.class);
        this.startService(activeClick);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSView), variables.cps));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        run = false;
        Intent activeClick = new Intent(this, ClickService.class);
        this.startService(activeClick);
        fullSave();
    }

    public void click(View clicker)
    {
        variables.clicks = variables.clicks + variables.clicksPC;
        TextView clickView = (TextView)findViewById(R.id.clickView);
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
    }

    public void viewCredits (View credits)
    {
        TextView creditsView  = (TextView)findViewById(R.id.creditsView);
        if (creditsView.getVisibility() == View.GONE)
        {
            creditsView.setVisibility(View.VISIBLE);
        }
        else if (creditsView.getVisibility() == View.VISIBLE)
        {
            creditsView.setVisibility(View.GONE);
        }
    }

    public void load()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
        variables.clicks = varReader.getInt("clicks", 0);
        variables.cps = varReader.getInt("cps", 0);
        variables.clicksPC = varReader.getInt("clicksPC", 1);
        variables.store1Num = varReader.getInt("store1Num", 0);
        variables.store1Price = varReader.getInt("store1Price", 1);
        variables.store2Num = varReader.getInt("store2Num", 0);
        variables.store2Price = varReader.getInt("store2Price", 2);
    }

    public void fullSave()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor varSaver = varReader.edit();
        varSaver.putInt("clicks", variables.clicks);
        varSaver.putInt("cps", variables.cps);
        varSaver.putInt("clicksPC", variables.clicksPC);
        varSaver.putInt("store1Num", variables.store1Num);
        varSaver.putInt("store1Price", variables.store1Price);
        varSaver.putInt("store2Num", variables.store2Num);
        varSaver.putInt("store2Price", variables.store2Price);
        varSaver.apply();
    }
}
