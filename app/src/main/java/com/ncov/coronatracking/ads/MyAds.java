package com.ncov.coronatracking.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class MyAds {
    public static void initInterAds(Context context){
        if(AdsConfigLoaded.get().getInShowInter().equals("0")){
            return;
        }
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            MyFacebookAdsController.initInterstitialAds(context);
        }else {
            MyAdmobController.initInterstitialAds(context);
        }
    }

    public static boolean isInterLoaded(){
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            return MyFacebookAdsController.isInterLoaded();
        }else {
            return MyAdmobController.isInterLoaded();
        }
    }

    public static void initBannerIds(Activity context){

        if(AdsConfigLoaded.get().getInShowBanner().equals("0")){
            return;
        }

        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            MyFacebookAdsController.initBannerAds(context);
        }else {
            MyAdmobController.initBannerAds(context);
        }
    }


    public static void initBannerQuickAccess(View view){

        if(AdsConfigLoaded.get().getSystemShowBanner().equals("0")){
            return;
        }

        if(AdsConfigLoaded.get().getSystemPlatForm().contains("facebook")){
            MyFacebookAdsController.initBannerQuickAccess(view);
        }else {
            MyAdmobController.initBannerQuickAccess(view);
        }
    }

    public static void showInterFull(Context context,Callback callback){
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            MyFacebookAdsController.showAdsFullBeforeDoAction(context,callback);
        }else {
            MyAdmobController.showAdsFullBeforeDoAction(context,callback);
        }
    }

    public static void showInterFullNow(Context context,Callback callback){
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            MyFacebookAdsController.showAdsFullNow(context,callback);
        }else {
            MyAdmobController.showAdsFullNow(context,callback);
        }
    }

    public static int getFlag() {
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            return MyFacebookAdsController.flagQC;
        }else {
            return MyAdmobController.flagQC;
        }
    }

    public static void initNativeMain(View view){
        if(AdsConfigLoaded.get().getInShowNative().equals("0")){
            return;
        }
        if(AdsConfigLoaded.get().getInAppPlatForm().contains("facebook")){
            MyFacebookAdsController.initNativeMain(view);
        }else {
            MyAdmobController.initNativeMain(view);
        }
    }

}
