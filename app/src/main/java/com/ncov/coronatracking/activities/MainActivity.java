package com.ncov.coronatracking.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ncov.coronatracking.App;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.ads.AdsConfigLoaded;
import com.ncov.coronatracking.ads.MyAds;
import com.ncov.coronatracking.fragments.InfoFragment;
import com.ncov.coronatracking.fragments.LiveMapFragment;
import com.ncov.coronatracking.fragments.MainFragment;
import com.ncov.coronatracking.helpers.CoronaDataHelper;
import com.ncov.coronatracking.views.UpdateDialog;

public class MainActivity extends AppCompatActivity {

    public View btnMain,btnLiveMap,btnInfo;

    private ViewPager pagerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}
        setContentView(R.layout.activity_main);

        App.get().sendTracker(getClass().getSimpleName());

        MyAds.initBannerIds(this);
        MyAds.initInterAds(this);

        CoronaDataHelper.get().syn(this);

        initPager();

        initBottomClick();

        checkAndroidQPermission();

        if(!AdsConfigLoaded.get().getUpdateMode().equals("0")){
            new UpdateDialog(this).showDialog();
        }


    }

    private void checkAndroidQPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                Dialog dialog = new Dialog(this,R.style.Translucent);
                dialog.setContentView(R.layout.dialog_overlay);
                dialog.setCancelable(false);
                dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
                dialog.findViewById(R.id.bt_give_permission).setOnClickListener(v -> {
                    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    myIntent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(myIntent, 101);
                    dialog.dismiss();
                });
                dialog.show();
            }
        }
    }

    private void initPager() {
        pagerMain = findViewById(R.id.pager_main);
        pagerMain.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),1) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    return MainFragment.newInstance();
                }else if(position==1){
                    return LiveMapFragment.newInstance();
                }else {
                    return InfoFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        pagerMain.setOffscreenPageLimit(3);
    }

    private void initBottomClick() {
        btnMain = findViewById(R.id.bt_statistics);
        btnLiveMap = findViewById(R.id.bt_live_map);
        btnInfo = findViewById(R.id.bt_info);
        click(btnMain);
        btnMain.setOnClickListener(v -> {
            MyAds.showInterFull(this, (value, where) -> {
                click(v);
                Statistic(v);
            });
        });
        btnLiveMap.setOnClickListener(v -> {
            MyAds.showInterFull(this, (value, where) -> {
                click(v);
                LiveMap(v);
            });

        });
        btnInfo.setOnClickListener(v -> {
            MyAds.showInterFull(this, (value, where) -> {
                click(v);
                Info(v);
            });

        });
    }

    public void Statistic(View view) {
        if(pagerMain.getCurrentItem()!=0){
            pagerMain.setCurrentItem(0,false);
        }
    }
    public void LiveMap(View view) {
        if(pagerMain.getCurrentItem()!=1){
            pagerMain.setCurrentItem(1,false);
        }
    }

    public void Info(View view) {
        if(pagerMain.getCurrentItem()!=2){
            pagerMain.setCurrentItem(2,false);
        }
    }


    private void click(View view) {
        if(btnMain.getScaleX()==1.05f){
            btnMain.animate().scaleX(0.9f).scaleY(0.9f).setDuration(200).start();
        }else {
            btnMain.animate().scaleX(0.9f).scaleY(0.9f).setDuration(0).start();
        }

        if(btnLiveMap.getScaleX()==1.05f){
            btnLiveMap.animate().scaleX(0.9f).scaleY(0.9f).setDuration(200).start();
        }else {
            btnLiveMap.animate().scaleX(0.9f).scaleY(0.9f).setDuration(0).start();
        }

        if(btnInfo.getScaleX()==1.05f){
            btnInfo.animate().scaleX(0.9f).scaleY(0.9f).setDuration(200).start();
        }else {
            btnInfo.animate().scaleX(0.9f).scaleY(0.9f).setDuration(0).start();
        }

        btnMain.setSelected(false);
        btnLiveMap.setSelected(false);
        btnInfo.setSelected(false);
        view.setSelected(true);
        view.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start();
    }

    private Boolean isExit = false;
    @Override
    public void onBackPressed() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if(index>0){
            super.onBackPressed();
        } else {
            if (isExit) {
                finish();
            } else {
                Toast.makeText(this,"Press again to exit!",Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(() -> isExit = false, 3 * 1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==101&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Oh no", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
