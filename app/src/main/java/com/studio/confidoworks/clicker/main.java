package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class main extends Activity
{
    TextView clickView = (TextView)findViewById(R.id.clickView);
    TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
    TextView creditsView  = (TextView)findViewById(R.id.creditsView);
    public static boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent activeClick = new Intent(this, ClickService.class);
        this.startService(activeClick);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        variables obj = new variables();
        obj.load();
        clickView.setText(variables.clicks + getString(R.string.clickView));
        clickPSView.setText(variables.cps + getString(R.string.clickPSView));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //ClickService obj = new ClickService();
        //obj.cps.removeCallbacksAndMessages(obj.addClicks);
        run = false;
        Intent activeClick = new Intent(this, ClickService.class);
        this.startService(activeClick);
    }

    public void click(View clicker)
    {
        variables.clicks = variables.clicks + variables.clicksPC;
        clickView.setText(variables.clicks + getString(R.string.clickView));
    }

    public void viewCredits (View credits)
    {
        if (creditsView.getVisibility() == View.GONE)
        {
            creditsView.setVisibility(View.VISIBLE);
        }
        else if (creditsView.getVisibility() == View.VISIBLE)
        {
            creditsView.setVisibility(View.GONE);
        }
    }
}
