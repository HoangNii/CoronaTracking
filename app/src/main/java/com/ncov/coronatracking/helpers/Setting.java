package com.ncov.coronatracking.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ncov.coronatracking.App;

public class Setting {

    public static boolean isQuickAccess(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.get());
        return preferences.getBoolean("quick_access",true);
    }

    public static void setQuickAccess(boolean is){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.get());
        preferences.edit().putBoolean("quick_access",is).apply();
    }
}
