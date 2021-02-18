package com.ncov.coronatracking.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import com.ncov.coronatracking.activities.QuickAccessActivity;
import com.ncov.coronatracking.helpers.Setting;

public class LockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction()+"";
        if(action.equals(Intent.ACTION_SCREEN_OFF)
                && Setting.isQuickAccess()){
            if(!isScreenOn(context)){
                Intent lockIntent = new Intent(context, QuickAccessActivity.class);
                lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_USER_ACTION
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION
                        | Intent.FLAG_FROM_BACKGROUND);
                context.getApplicationContext().startActivity(lockIntent);
            }
        }
    }

    private boolean isScreenOn(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isInteractive();
        }
        return isScreenOn;
    }
}
