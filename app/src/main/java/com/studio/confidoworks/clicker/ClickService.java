package com.studio.confidoworks.clicker;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

public class ClickService extends IntentService
{
    Handler cps = new Handler();
    Runnable addClicks = new Runnable()
    {
        public void run()
        {
            variables.clicks = variables.clicks + variables.cps;
        }
    };
    public ClickService()
    {
        super("ClickService");
    }
    @Override
    protected void onHandleIntent(Intent activeClick)
    {
        if (!main.run)
        {
            cps.removeCallbacksAndMessages(addClicks);
        }
        else
        {
            while (main.run)
            {
                cps.postDelayed(addClicks, 1000);
            }
        }
    }
}
