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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static android.app.ActivityManager.isUserAMonkey;
import static com.studio.confidoworks.clicker.variables.clicks;
import static com.studio.confidoworks.clicker.variables.clicksPC;
import static com.studio.confidoworks.clicker.variables.cps;
import static com.studio.confidoworks.clicker.variables.store1Price;
import static com.studio.confidoworks.clicker.variables.store2Price;
import static com.studio.confidoworks.clicker.variables.store3Price;

public class main extends Activity
{
    private static final String PREFS_NAME_MAIN="clicker";
    private static final String PREFS_NAME_STORE="store";
    private static final String PREFS_NAME_TIME="time";

    TextView clickView;
    TextView clickPSView;

    static MathContext mc = new MathContext(0, RoundingMode.CEILING);

    private final Context app = this;
    private boolean newStoreOpened = false;
    private boolean showNotifier = false;
    private final Handler update = new Handler();
    private final Runnable updateClicks = new Runnable()
    {
        public void run()
        {
            if (variables.run)
            {
                clickView.setText(String.format(getResources().getString(R.string.clickView),
                        clicks.toString()));
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
        SharedPreferences timeRead = app.getSharedPreferences(PREFS_NAME_TIME, 0);
        final long timeStopped = timeRead.getLong("stopped", System.currentTimeMillis());
        if (timeStopped != System.currentTimeMillis()) showNotifier = true;
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
        if (newStoreOpened) showNotifier = false;
        newStoreOpened = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        clickView.setText(String.format(getResources().getString(R.string.clickView),
                clicks.toString()));
        clickPSView.setText(String.format(getResources().getString(R.string.clickPSView),
                cps.toString()));
        storeAvailability();
        update.post(updateClicks);
        timeAwayNotifier();
        if (isUserAMonkey())
        {
            SharedPreferences run = app.getSharedPreferences("run", 0);
            String message = run.getString("run", null);
            Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
        }
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
            SharedPreferences.Editor timeSave = app.getSharedPreferences(PREFS_NAME_TIME, 0).edit();
            timeSave.putLong("stopped", System.currentTimeMillis());
            timeSave.apply();
        }
        save();
        if (isUserAMonkey())
        {
            SharedPreferences.Editor run = app.getSharedPreferences("run", 0).edit();
            run.putString("run", "AAAHHH IT'S A MONKEY RUUUN!!!");
            run.apply();
        }
    }

    public void click(View clicker)
    {
        clicks = clicks.add(clicksPC);
        clickView.setText(String.format(getResources().getString(R.string.clickView),
                clicks.toString()));
        storeAvailability();
    }

    public void openStore (View store)
    {
        Intent newStore = new Intent(this, store.class);
        newStoreOpened = true;
        showNotifier = false;
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

    private void timeAwayNotifier()
    {
        SharedPreferences timeRead = app.getSharedPreferences(PREFS_NAME_TIME, 0);
        final long timeStopped = timeRead.getLong("stopped", System.currentTimeMillis());
        BigDecimal timeAway = BigDecimal.valueOf(System.currentTimeMillis() - timeStopped);
        timeAway = timeAway.divide(new BigDecimal("1000"), mc);
        timeAway = timeAway.setScale(0, RoundingMode.CEILING);
        final BigDecimal clicksToAdd = cps.multiply(timeAway);
        if (isUserAMonkey())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.dialogConfirm, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface notifier, int id)
                {
                    clicks = clicks.add(clicksToAdd);
                    clickView.setText(String.format(getResources().getString(R.string.clickView),
                            clicks.toString()));
                }
            });
            builder.setMessage(String.format(getResources().getString(R.string.dialogMessage), clicksToAdd));
            AlertDialog notifier = builder.create();
            notifier.show();
        }
        else if (!showNotifier) update.post(updateClicks);
        else if ((timeAway.compareTo(new BigDecimal("300")) > 0))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.dialogConfirm, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface notifier, int id)
                {
                    clicks = clicks.add(clicksToAdd);
                    clickView.setText(String.format(getResources().getString(R.string.clickView),
                            clicks.toString()));
                    showNotifier = false;
                }
            });
            builder.setMessage(String.format(getResources().getString(R.string.dialogMessage), clicksToAdd));
            AlertDialog notifier = builder.create();
            notifier.show();
        }
        else if ((timeAway.compareTo(new BigDecimal("300")) <= 0))
        {
            clicks = clicks.add(clicksToAdd);
            clickView.setText(String.format(getResources().getString(R.string.clickView), clicks
                    .toString()));
            showNotifier = false;
        }
    }

    private void storeAvailability()
    {
        Button store = (Button)findViewById(R.id.store);
        SharedPreferences storeReader = app.getSharedPreferences(PREFS_NAME_STORE, 0);
        store1Price = new BigDecimal(storeReader.getString("store1Price", "5"));
        store2Price = new BigDecimal(storeReader.getString("store2Price", "10"));
        store3Price = new BigDecimal(storeReader.getString("store3Price", "15"));
        if ((store1Price.compareTo(clicks)) <= 0) store.setEnabled(true);
        else if ((store2Price.compareTo(clicks)) <= 0) store.setEnabled(true);
        else if ((store3Price.compareTo(clicks)) <= 0) store.setEnabled(true);
        else store.setEnabled(false);
    }

    private void load()
    {
        SharedPreferences varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0);
        variables.run = varReader.getBoolean("run", false);
        clicks = new BigDecimal(varReader.getString("clicks", "0"));
        cps = new BigDecimal(varReader.getString("cps", "0"));
        clicksPC = new BigDecimal(varReader.getString("clicksPC", "1"));
    }

    private void save()
    {
        SharedPreferences.Editor varReader = app.getSharedPreferences(PREFS_NAME_MAIN, 0).edit();
        varReader.putBoolean("run", variables.run);
        varReader.putString("clicks", clicks.toString());
        varReader.putString("cps", cps.toString());
        varReader.putString("clicksPC", clicksPC.toString());
        varReader.apply();
    }
}