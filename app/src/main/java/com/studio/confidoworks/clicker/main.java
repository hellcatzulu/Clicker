package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class main extends Activity
{
    TextView creditsView  = (TextView)findViewById(R.id.creditsView);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
