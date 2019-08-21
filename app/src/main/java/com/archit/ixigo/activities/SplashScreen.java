package com.archit.ixigo.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import com.archit.ixigo.MainActivity;
import com.archit.ixigo.R;

/**
 * Splash screen before starting main activity
 */
public class SplashScreen extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1600);

    }

}