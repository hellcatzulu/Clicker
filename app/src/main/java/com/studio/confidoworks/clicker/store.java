package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class store extends Activity
{
    private static final String PREFS_NAME_MAIN="clicker";
    private static final String PREFS_NAME_STORE="store";
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
                storeLoadAvailability();
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
        storeLoadView();
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
        ProgressBar store1Progress = (ProgressBar)findViewById(R.id.store1Progress);
        if (variables.clicks > variables.store1Price)
        {
            variables.clicks = variables.clicks - variables.store1Price;
            variables.clicksPC = variables.clicksPC + 1;
            variables.store1Num = variables.store1Num + 1;
            double store1Price = variables.store1Price;
            store1Price = store1Price * 1.5;
            variables.store1Price = (int)Math.ceil(store1Price);
            fullSave();
            clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
            store1Cost.setText(String.format(getResources().getString(R.string.store1Cost), variables.store1Price));
            store1Bought.setText(String.format(getResources().getString(R.string.store1Bought), variables.store1Num));
            store1Progress.setProgress(variables.store1Num);
            storeLoadAvailability();
        }
    }

    public void store2Buy(View store2Buy)
    {
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        TextView store2Cost = (TextView)findViewById(R.id.store2Cost);
        TextView store2Bought = (TextView)findViewById(R.id.store2Bought);
        ProgressBar store2Progress = (ProgressBar)findViewById(R.id.store2Progress);
        if (variables.clicks > variables.store2Price)
        {
            variables.clicks = variables.clicks - variables.store2Price;
            variables.cps = variables.cps + 1;
            variables.store2Num = variables.store2Num + 1;
            double store2Price = variables.store2Price;
            store2Price = store2Price * 1.5;
            variables.store2Price = (int)Math.ceil(store2Price);
            fullSave();
            clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
            clickPSView.setText(String.format(getResources().getString(R.string.clickPSViewStore), variables.cps));
            store2Cost.setText(String.format(getResources().getString(R.string.store2Cost), variables.store2Price));
            store2Bought.setText(String.format(getResources().getString(R.string.store2Bought), variables.store2Num));
            store2Progress.setProgress(variables.store2Num);
            storeLoadAvailability();
        }
    }

    public void store3Buy(View store3Buy)
    {
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        TextView store3Cost = (TextView)findViewById(R.id.store3Cost);
        TextView store3Bought = (TextView)findViewById(R.id.store3Bought);
        ProgressBar store3Progress = (ProgressBar)findViewById(R.id.store3Progress);
        if (variables.clicks > variables.store3Price)
        {
            variables.clicks = variables.clicks - variables.store3Price;
            variables.cps = variables.cps + 2;
            variables.store3Num = variables.store3Num + 1;
            double store3Price = variables.store3Price;
            store3Price = store3Price * 1.5;
            variables.store3Price = (int)Math.ceil(store3Price);
            fullSave();
            clickView.setText(String.format(getResources().getString(R.string.clickViewStore), variables.clicks));
            clickPSView.setText(String.format(getResources().getString(R.string.clickPSViewStore), variables.cps));
            store3Cost.setText(String.format(getResources().getString(R.string.store3Cost), variables.store3Price));
            store3Bought.setText(String.format(getResources().getString(R.string.store3Bought), variables.store3Num));
            store3Progress.setProgress(variables.store3Num);
            storeLoadAvailability();
        }
    }

    private void storeLoad()
    {
        SharedPreferences storeReader = app.getSharedPreferences(PREFS_NAME_STORE, 0);
        variables.store1Num = storeReader.getInt("store1Num", 0);
        variables.store1Price = storeReader.getInt("store1Price", 5);
        variables.store2Num = storeReader.getInt("store2Num", 0);
        variables.store2Price = storeReader.getInt("store2Price", 10);
        variables.store3Num = storeReader.getInt("store3Num", 0);
        variables.store3Price = storeReader.getInt("store3Price", 15);
    }

    private void storeLoadView()
    {
        TextView store1Cost = (TextView)findViewById(R.id.store1Cost);
        TextView store1Bought = (TextView)findViewById(R.id.store1Bought);
        ProgressBar store1Progress = (ProgressBar)findViewById(R.id.store1Progress);
        store1Cost.setText(String.format(getResources().getString(R.string.store1Cost), variables.store1Price));
        store1Bought.setText(String.format(getResources().getString(R.string.store1Bought), variables.store1Num));
        store1Progress.setProgress(variables.store1Num);

        TextView store2Cost = (TextView)findViewById(R.id.store2Cost);
        TextView store2Bought = (TextView)findViewById(R.id.store2Bought);
        ProgressBar store2Progress = (ProgressBar)findViewById(R.id.store2Progress);
        store2Cost.setText(String.format(getResources().getString(R.string.store2Cost), variables.store2Price));
        store2Bought.setText(String.format(getResources().getString(R.string.store2Bought), variables.store2Num));
        store2Progress.setProgress(variables.store2Num);

        TextView store3Cost = (TextView)findViewById(R.id.store3Cost);
        TextView store3Bought = (TextView)findViewById(R.id.store3Bought);
        ProgressBar store3Progress = (ProgressBar)findViewById(R.id.store3Progress);
        store3Cost.setText(String.format(getResources().getString(R.string.store3Cost), variables.store3Price));
        store3Bought.setText(String.format(getResources().getString(R.string.store3Bought), variables.store3Num));
        store3Progress.setProgress(variables.store3Num);
    }

    private void storeLoadAvailability()
    {
        LinearLayout store1 = (LinearLayout)findViewById(R.id.store1);
        Button store1Buy = (Button)findViewById(R.id.store1Buy);
        ProgressBar store1Progress = (ProgressBar)findViewById(R.id.store1Progress);

        LinearLayout store2 = (LinearLayout)findViewById(R.id.store2);
        Button store2Buy = (Button)findViewById(R.id.store2Buy);
        ProgressBar store2Progress = (ProgressBar)findViewById(R.id.store2Progress);

        LinearLayout store3 = (LinearLayout)findViewById(R.id.store3);
        Button store3Buy = (Button)findViewById(R.id.store3Buy);
        ProgressBar store3Progress = (ProgressBar)findViewById(R.id.store3Progress);

        if (variables.clicks <= variables.store1Price) store1Buy.setEnabled(false);
        else if (variables.store1Num == 50) store1.setVisibility(View.GONE);
        else store1Buy.setEnabled(true);
        if (variables.store1Num == 50) store1Progress.setVisibility(View.GONE);

        if (variables.clicks <= variables.store2Price) store2Buy.setEnabled(false);
        else if (variables.store2Num == 1000) store2.setVisibility(View.GONE);
        else store2Buy.setEnabled(true);
        if (variables.store2Num == 1000) store2Progress.setVisibility(View.GONE);

        if (variables.clicks <= variables.store3Price) store3Buy.setEnabled(false);
        else if (variables.store3Num == 1000) store3.setVisibility(View.GONE);
        else store3Buy.setEnabled(true);
        if (variables.store3Num == 1000) store3Progress.setVisibility(View.GONE);
    }

    private void load()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
        variables.run = varReader.getBoolean("run", false);
        variables.clicks = varReader.getInt("clicks", 0);
        variables.cps = varReader.getInt("cps", 0);
        variables.clicksPC = varReader.getInt("clicksPC", 1);
    }

    private void fullSave()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
        SharedPreferences.Editor varSaver = varReader.edit();
        varSaver.putInt("clicks", variables.clicks);
        varSaver.putInt("cps", variables.cps);
        varSaver.putInt("clicksPC", variables.clicksPC);
        varSaver.apply();

        SharedPreferences storeReader = app.getSharedPreferences(PREFS_NAME_STORE, 0);
        SharedPreferences.Editor storeSaver = storeReader.edit();
        storeSaver.putInt("store1Num", variables.store1Num);
        storeSaver.putInt("store1Price", variables.store1Price);
        storeSaver.putInt("store2Num", variables.store2Num);
        storeSaver.putInt("store2Price", variables.store2Price);
        storeSaver.putInt("store3Num", variables.store3Num);
        storeSaver.putInt("store3Price", variables.store3Price);
        storeSaver.apply();
    }
}