package com.ncov.coronatracking;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.flurry.android.FlurryAgent;
import com.ncov.coronatracking.helpers.Setting;
import com.ncov.coronatracking.services.QuickAccessService;

public class App extends Application {

    private static App app;

    public static App get() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        if(Setting.isQuickAccess()){
            startQuickAccess();
        }

        new FlurryAgent.Builder().build(this, "WC53HSCH5HVSBRN9M27B");

        AudienceNetworkAds.initialize(this);

        if (BuildConfig.DEBUG) {
            AdSettings.setDebugBuild(true);
        }
    }


    public void sendTracker(String s){
        FlurryAgent.logEvent(s);
    }

    public void stopQuickAccess() {
        stopService(new Intent(this, QuickAccessService.class));
    }

    public void startQuickAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, QuickAccessService.class));
        }else {
            startService(new Intent(this, QuickAccessService.class));
        }
    }

}
