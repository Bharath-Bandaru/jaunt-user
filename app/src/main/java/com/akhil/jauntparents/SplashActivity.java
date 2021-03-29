package com.akhil.jauntparents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Bharath on 14/01/17.
 */

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String openstatus = "openstatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if(sharedpreferences.getBoolean(openstatus,true)) {
                    Intent i = new Intent(SplashActivity.this, DetailsActivity.class);
                    startActivity(i);
                    finish();
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putBoolean(openstatus, false);
//                    editor.commit();
                }else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
