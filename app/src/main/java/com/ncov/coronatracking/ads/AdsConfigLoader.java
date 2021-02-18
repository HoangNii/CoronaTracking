 package com.ncov.coronatracking.ads;

import com.flurry.android.FlurryConfig;
import com.flurry.android.FlurryConfigListener;

 public class AdsConfigLoader {

     private Callback callback;

     public AdsConfigLoader(Callback callback) {
         this.callback = callback;
     }

     public void syn(){
         final FlurryConfig mFlurryConfig = FlurryConfig.getInstance();
         mFlurryConfig.registerListener(new FlurryConfigListener() {
             @Override
             public void onFetchSuccess() {
                 mFlurryConfig.activateConfig();
             }

             @Override
             public void onFetchNoChange() {
                 if(callback!=null){
                     callback.callBack(0,0);
                     callback = null;
                 }
             }

             @Override
             public void onFetchError(boolean b) {
                 if(callback!=null){
                     callback.callBack(0,0);
                     callback = null;
                 }
             }

             @Override
             public void onActivateComplete(boolean b) {
                 String update_link = mFlurryConfig.getString("update_link","Update");
                 String update_message = mFlurryConfig.getString("update_message","Update");
                 String update_action_title = mFlurryConfig.getString("update_action_title","Update");
                 String update_mode =  mFlurryConfig.getString("update_mode","Update");
                 String update_title = mFlurryConfig.getString("update_title","Update");
                 String update_icon = mFlurryConfig.getString("update_icon","Update");
                 String update_is_ad = mFlurryConfig.getString("update_is_ad","Update");
                 String target_id = mFlurryConfig.getString("target_id","Update");
                 String ads_frequency = mFlurryConfig.getString("ads_frequency","30");
                 AdsConfigLoaded.get().setUpdate(
                         update_link,
                         update_message,
                         update_action_title,
                         update_mode,
                         update_title,
                         update_icon,
                         update_is_ad,
                         target_id,
                         ads_frequency

                 );

                 String ads_platform  = mFlurryConfig.getString("ads_platform","admod");
                 String show_banner = mFlurryConfig.getString("show_banner","1");
                 String show_inter = mFlurryConfig.getString("show_inter","1");
                 String show_native  =  mFlurryConfig.getString("show_native","1");
                 String show_reward  = mFlurryConfig.getString("show_reward","1");
                 String inter_start  = mFlurryConfig.getString("inter_start","0");
                 String inter_delay  = mFlurryConfig.getString("inter_delay","30");
                 String banner_id  = mFlurryConfig.getString("banner_id","ca-app-pub-5037686413264480/8387361296");
                 String inter_id  = mFlurryConfig.getString("inter_id","ca-app-pub-5037686413264480/7066734283");
                 String native_id  = mFlurryConfig.getString("native_id","ca-app-pub-5037686413264480/7022502255");
                 String reward_id  = mFlurryConfig.getString("reward_id","0");
                 String ads_id  = mFlurryConfig.getString("ads_id","0");
                 AdsConfigLoaded.get().setInApp(
                         ads_platform,show_banner,show_inter,show_native,show_reward,inter_start,inter_delay,banner_id,
                         inter_id,native_id,reward_id,ads_id
                 );

                 String s_ads_platform  = mFlurryConfig.getString("s_ads_platform","admod");
                 String s_show_banner = mFlurryConfig.getString("s_show_banner","1");
                 String s_show_inter = mFlurryConfig.getString("s_show_inter","1");
                 String s_show_native  =  mFlurryConfig.getString("s_show_native","1");
                 String s_show_reward  = mFlurryConfig.getString("s_show_reward","1");
                 String s_banner_id  = mFlurryConfig.getString("s_banner_id","ca-app-pub-5037686413264480/8379815954");
                 String s_inter_id  = mFlurryConfig.getString("s_inter_id","");
                 String s_native_id  = mFlurryConfig.getString("s_native_id","");
                 String s_reward_id  = mFlurryConfig.getString("s_reward_id","0");

                 AdsConfigLoaded.get().setSystem(
                         s_ads_platform,s_show_banner,s_show_inter,s_show_native,s_show_reward,s_banner_id,s_inter_id,s_native_id,
                         s_reward_id);

                 if(callback!=null){
                     callback.callBack(0,0);
                     callback = null;
                 }

             }
         });
         mFlurryConfig.fetchConfig();
     }

 }
