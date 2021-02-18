package com.ncov.coronatracking.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.ads.AdsConfigLoaded;
import java.util.Random;

public class UpdateDialog extends Dialog {

    public UpdateDialog(@NonNull Context context) {
        super(context, R.style.Translucent);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_update);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvMessage = findViewById(R.id.tv_message);
        TextView tvTime = findViewById(R.id.tv_time);
        ImageView imgIcon = findViewById(R.id.img_icon);

        TextView btnCancel = findViewById(R.id.bt_cancel);
        TextView btnOk = findViewById(R.id.bt_ok);

        tvTime.setText(getStringDateSms(System.currentTimeMillis()));
        tvMessage.setText(AdsConfigLoaded.get().getUpdateMessage());
        tvTitle.setText(AdsConfigLoaded.get().getUpdateTitle());
        btnOk.setText(AdsConfigLoaded.get().getUpdateActionTitle());
        Glide.with(getContext())
                .load(AdsConfigLoaded.get().getUpdateIcon())
                .into(imgIcon);
        btnCancel.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        if(AdsConfigLoaded.get().getUpdateMode().equals("2")){
            setCancelable(false);
            btnCancel.setVisibility(View.GONE);
        }
        btnCancel.setOnClickListener(v -> dismiss());
        btnOk.setOnClickListener(v -> goToStore(getContext(),AdsConfigLoaded.get().getUpdateLink()));

        if(!AdsConfigLoaded.get().getUpdateIsAd().equals("1")){
            findViewById(R.id.tv_ads).setVisibility(View.GONE);
        }

    }

    public void showDialog(){
        if(!AdsConfigLoaded.get().getUpdateIsAd().equals("1")){
            show();
        }else {
            boolean isInstall = isPackageInstalled(AdsConfigLoaded.get().getUpdateTargetId(),getContext().getPackageManager());
            if(!isInstall){
                int fr = new Random().nextInt(100);
                try {
                    if(fr<Integer.parseInt(AdsConfigLoaded.get().getUpdateAdsFrequency())){
                        show();
                    }
                }catch (Exception e){
                    System.out.print(e.getMessage());
                    show();
                }
            }

        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void goToStore(Context context, String url) {
        if(url.contains("https://play.google.com/")){
            String MARKET_DETAILS_ID = "market://details?id=";
            String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
            String link = url.replace(PLAY_STORE_LINK,"");
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID +link))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK +link))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

    }

    private static String getStringDateSms(long date) {
        return DateFormat.format("HH:mm aa",date).toString();
    }

}
