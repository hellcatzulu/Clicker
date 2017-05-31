package com.studio.confidoworks.clicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.studio.confidoworks.clicker.variables.*;

public class main extends Activity
{
    TextView clickView = (TextView)findViewById(R.id.clickView);
    TextView clickPSView = (TextView)findViewById(R.id.clickPSView);
    TextView creditsView  = (TextView)findViewById(R.id.creditsView);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
