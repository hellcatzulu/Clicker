/*
 * Copyright (c) 2017 ConfidoWorks Studio (MIT License)
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class main extends Activity
{
    private static final String PREFS_NAME_MAIN="clicker";
    private static final String PREFS_NAME_STORE="store";

    TextView clickView;
    TextView clickPSView;

    private final Context app = this;
    private boolean newStoreOpened = false;
    private long timeStopped;
    private boolean showTimeAway = false;
    private final Handler update = new Handler();
    private final Runnable updateClicks = new Runnable()
    {
        public void run()
        {
            if (variables.run)
            {
                clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
                storeAvailability();
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
        clickView = (TextView)findViewById(R.id.clickView);
        clickPSView = (TextView)findViewById(R.id.clickPSView);
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
        if (showTimeAway)
        {
            long timeAway = System.currentTimeMillis() - timeStopped;
            timeAway = timeAway / 1000;
            final long clicksToAdd = timeAway * variables.cps;
            if (timeAway > 300) timeAwayNotifier();
            else variables.clicks = variables.clicks + clicksToAdd;
        }
        showTimeAway = false;
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        newStoreOpened = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSView), variables.cps));
        storeAvailability();
        update.post(updateClicks);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        save();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (!newStoreOpened)
        {
            variables.run = false;
            showTimeAway = true;
            SharedPreferences timeSave = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
            SharedPreferences.Editor timeSaver = timeSave.edit();
            timeSaver.putLong("stopped", System.currentTimeMillis());
            timeSaver.putBoolean("showTimeAway", showTimeAway);
            timeSaver.apply();
        }
        save();
    }

    public void click(View clicker)
    {
        variables.clicks = variables.clicks + variables.clicksPC;
        clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
        storeAvailability();
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

    private Dialog timeAwayNotifier()
    {
        long timeAway = System.currentTimeMillis() - timeStopped;
        timeAway = timeAway / 1000;
        final long clicksToAdd = timeAway * variables.cps;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.dialogConfirm, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface notifier, int id)
            {
                variables.clicks = variables.clicks + clicksToAdd;
                clickView.setText(String.format(getResources().getString(R.string.clickView), variables.clicks));
                clickPSView.setText(String.format(getResources().getString(R.string.clickPSView), variables.cps));
            }
        });
        builder.setMessage(String.format(getResources().getString(R.string.dialogMessage), clicksToAdd));
        return builder.create();
    }

    private void storeAvailability()
    {
        Button store = (Button)findViewById(R.id.store);
        SharedPreferences storeReader = app.getSharedPreferences(PREFS_NAME_STORE, 0);
        variables.store1Price = storeReader.getLong("store1Price", 5);
        variables.store2Price = storeReader.getLong("store2Price", 10);
        variables.store3Price = storeReader.getLong("store3Price", 15);
        if (variables.store1Price <= variables.clicks) store.setEnabled(true);
        else if (variables.store2Price <= variables.clicks) store.setEnabled(true);
        else if (variables.store3Price <= variables.clicks) store.setEnabled(true);
        else store.setEnabled(false);
    }

    private void load()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
        variables.run = varReader.getBoolean("run", false);
        variables.clicks = varReader.getLong("clicks", 0);
        variables.cps = varReader.getLong("cps", 0);
        variables.clicksPC = varReader.getLong("clicksPC", 1);
        timeStopped = varReader.getLong("stopped", 0);
        showTimeAway = varReader.getBoolean("showTimeAway", false);
    }

    private void save()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
        SharedPreferences.Editor varSaver = varReader.edit();
        varSaver.putBoolean("run", variables.run);
        varSaver.putLong("clicks", variables.clicks);
        varSaver.putLong("cps", variables.cps);
        varSaver.putLong("clicksPC", variables.clicksPC);
        varSaver.apply();
    }
}
