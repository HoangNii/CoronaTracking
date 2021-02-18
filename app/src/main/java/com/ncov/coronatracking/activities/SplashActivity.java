package com.ncov.coronatracking.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.ncov.coronatracking.App;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.ads.AdsConfigLoader;
import com.ncov.coronatracking.ads.MyAds;
import com.ncov.coronatracking.helpers.CoronaDownloader;

public class SplashActivity extends AppCompatActivity {

    private int delay = 1000;

    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}
        setContentView(R.layout.activity_splash);

        App.get().sendTracker(getClass().getSimpleName());

        if(!MyAds.isInterLoaded()){
            MyAds.initInterAds(this);
            delay = 4000;
        }

        CoronaDownloader.get().load(new CoronaDownloader.DownLoadCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    startMain();
                });

            }

            @Override
            public void onFail() {
                runOnUiThread(() -> {
                    startMain();
                });
            }
        });

    }

    private void startMain(){
        new AdsConfigLoader((value, where) -> new Handler().postDelayed(() -> {
            if(isRunning){
                MyAds.showInterFullNow(SplashActivity.this, (value1, where1) -> {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }else {
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        },delay)).syn();

    }
    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

}
