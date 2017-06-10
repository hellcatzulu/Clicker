package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class main extends Activity
{
    private static final String PREFS_NAME="save";
    private final Context app = this;
    private boolean newStoreOpened = false;
    private final Handler update = new Handler();
    private final Runnable updateClicks = new Runnable()
    {
        public void run()
        {
            if (variables.run)
            {
                TextView clickView = (TextView)findViewById(R.id.clickView);
                clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
                update.postDelayed(updateClicks, 500);
            }
            else if (!variables.run) update.removeCallbacksAndMessages(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        load();
        if (!variables.run)
        {
            variables.run = true;
            Intent activeClick = new Intent(this, ClickService.class);
            this.startService(activeClick);
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        newStoreOpened = false;
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSView), variables.cps));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TextView clickView = (TextView)findViewById(R.id.clickView);
        TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSView), variables.cps));
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
        if (!newStoreOpened) variables.run = false;
        fullSave();
    }

    public void click(View clicker)
    {
        variables.clicks = variables.clicks + variables.clicksPC;
        TextView clickView = (TextView)findViewById(R.id.clickView);
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
    }

    public void openStore (View store)
    {
        Intent newStore = new Intent(this, store.class);
        newStoreOpened = true;
        this.startActivity(newStore);
    }

    public void viewCredits (View credits)
    {
        TextView creditsView1 = (TextView)findViewById(R.id.creditsView1);
        TextView creditsView2 = (TextView)findViewById(R.id.creditsView2);
        TextView creditsView3 = (TextView)findViewById(R.id.creditsView3);
        if (creditsView1.getVisibility() == View.GONE)
        {
            creditsView1.setVisibility(View.VISIBLE);
            creditsView2.setVisibility(View.VISIBLE);
            creditsView3.setVisibility(View.VISIBLE);
        }
        else if (creditsView1.getVisibility() == View.VISIBLE)
        {
            creditsView1.setVisibility(View.GONE);
            creditsView2.setVisibility(View.GONE);
            creditsView3.setVisibility(View.GONE);
        }
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
        varSaver.putLong("stopped", System.currentTimeMillis());
        varSaver.putBoolean("run", variables.run);
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
