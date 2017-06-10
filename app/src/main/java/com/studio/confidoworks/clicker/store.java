package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class store extends Activity
{
    private static final String PREFS_NAME="save";
    private final Context app = this;
    private final Handler update = new Handler();
    private final Runnable updateClicks = new Runnable()
    {
        public void run()
        {
            if (variables.run)
            {
                TextView clickView = (TextView)findViewById(R.id.clickView);
                clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
                update.postDelayed(updateClicks, 500);
            }
            else if (!variables.run) update.removeCallbacksAndMessages(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        load();
        storeLoad();
        storeLoadAvailability();
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSViewStore), variables.cps));
        update.post(updateClicks);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        fullSave();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        fullSave();
    }

    public void store1Buy(View store1Buy)
    {
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView store1Cost = (TextView)findViewById(R.id.store1Cost);
        TextView store1Bought = (TextView)findViewById(R.id.store1Bought);
        if (variables.clicks > variables.store1Price)
        {
            variables.clicks = variables.clicks - variables.store1Price;
            variables.clicksPC = variables.clicksPC + 1;
            variables.store1Num = variables.store1Num + 1;
            variables.store1Price = variables.store1Price + 1;
            fullSave();
            clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
            store1Cost.setText(String.format(getResources().getString(R.string.store1Cost), variables.store1Price));
            store1Bought.setText(String.format(getResources().getString(R.string.store1Bought), variables.store1Num));
            storeLoadAvailability();
        }
    }

    public void store2Buy(View store2Buy)
    {
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        TextView store2Cost = (TextView)findViewById(R.id.store2Cost);
        TextView store2Bought = (TextView)findViewById(R.id.store2Bought);
        if (variables.clicks > variables.store2Price)
        {
            variables.clicks = variables.clicks - variables.store2Price;
            variables.cps = variables.cps + 1;
            variables.store2Num = variables.store2Num + 1;
            variables.store2Price = variables.store2Price + 2;
            fullSave();
            clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
            clickPSView.setText(String.format(getResources().getString(R.string.clickPSViewStore), variables.cps));
            store2Cost.setText(String.format(getResources().getString(R.string.store2Cost), variables.store2Price));
            store2Bought.setText(String.format(getResources().getString(R.string.store2Bought), variables.store2Num));
            storeLoadAvailability();
        }
    }

    private void storeLoad()
    {
        TextView store1Cost = (TextView)findViewById(R.id.store1Cost);
        TextView store1Bought = (TextView)findViewById(R.id.store1Bought);
        store1Cost.setText(String.format(getResources().getString(R.string.store1Cost), variables.store1Price));
        store1Bought.setText(String.format(getResources().getString(R.string.store1Bought), variables.store1Num));

        TextView store2Cost = (TextView)findViewById(R.id.store2Cost);
        TextView store2Bought = (TextView)findViewById(R.id.store2Bought);
        store2Cost.setText(String.format(getResources().getString(R.string.store2Cost), variables.store2Price));
        store2Bought.setText(String.format(getResources().getString(R.string.store2Bought), variables.store2Num));
    }

    private void storeLoadAvailability()
    {
        Button store1Buy = (Button)findViewById(R.id.store1Buy);
        Button store2Buy = (Button)findViewById(R.id.store2Buy);
        if (variables.clicks < variables.store1Price) store1Buy.setEnabled(false);
        else store1Buy.setEnabled(true);
        if (variables.clicks < variables.store2Price) store2Buy.setEnabled(false);
        else store2Buy.setEnabled(true);
    }

    private void load()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME, 0);
        variables.run = varReader.getBoolean("run", false);
        variables.clicks = varReader.getInt("clicks", 0);
        variables.cps = varReader.getInt("cps", 0);
        variables.clicksPC = varReader.getInt("clicksPC", 1);
        variables.store1Num = varReader.getInt("store1Num", 0);
        variables.store1Price = varReader.getInt("store1Price", 1);
        variables.store2Num = varReader.getInt("store2Num", 0);
        variables.store2Price = varReader.getInt("store2Price", 2);
    }

    private void fullSave()
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
