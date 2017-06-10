package com.studio.confidoworks.clicker;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

public class ClickService extends IntentService
{
    private final Handler cps = new Handler();
    private final Runnable addClicks = new Runnable()
    {
        public void run()
        {
            variables.clicks = variables.clicks + variables.cps;
            if (!variables.run) cps.removeCallbacksAndMessages(null);
            else if (variables.run) cps.postDelayed(addClicks, 1000);
        }
    };
    public ClickService()
    {
        super("ClickService");
    }
    @Override
    protected void onHandleIntent(Intent activeClick)
    {
        cps.postDelayed(addClicks, 1000);
    }
}
