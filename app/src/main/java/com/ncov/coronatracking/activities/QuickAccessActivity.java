package com.ncov.coronatracking.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.ncov.coronatracking.App;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.ads.MyAds;
import com.ncov.coronatracking.helpers.CoronaDataHelper;
import com.ncov.coronatracking.helpers.CoronaDownloader;
import com.ncov.coronatracking.models.CoronaMain;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuickAccessActivity extends AppCompatActivity {

    private TextView tvDate;

    private View itemView;

    private View viewLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noStatusBarAndNavigation();
        super.onCreate(savedInstanceState);
        clearStatusBarAndNavigationBar();
        try { setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch (Exception ignore) { }

        setContentView(R.layout.activity_quick_access);

        App.get().sendTracker(getClass().getSimpleName());

        ViewPager2 pager = findViewById(R.id.pager_lock);
        pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        pager.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(QuickAccessActivity.this).inflate(R.layout.item_quick_access,pager,false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if(position==1){
                    holder.itemView.setVisibility(View.GONE);
                }else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    tvDate = holder.itemView.findViewById(R.id.tv_date);
                    tvDate.setText(new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH).format(new Date()));
                    MyAds.initBannerQuickAccess(holder.itemView);
                    init(holder.itemView);
                }

            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==1){
                    new Handler().postDelayed(() -> {
                        finish();
                    },300);
                }
            }
        });
    }

    private void init(View view) {
        itemView = view;
        viewLoad = view.findViewById(R.id.view_load);
        loadData();
    }

    private void loadData() {
        CoronaDataHelper.get().getMain(this, (model, coronaCountryModels) -> {
            getData(model);
            viewLoad.setVisibility(View.GONE);
        });
    }

    @SuppressLint("SetTextI18n")
    private void getData(CoronaMain model) {
        DecimalFormat format = new DecimalFormat("#.##");
        TextView tvConfirmed = itemView.findViewById(R.id.tv_confirmed);
        TextView tvDeaths= itemView.findViewById(R.id.tv_deaths);
        TextView tvRecovered = itemView.findViewById(R.id.tv_recovered);
        TextView tvCountries = itemView.findViewById(R.id.tv_country);
        TextView tvPercentageDeath = itemView.findViewById(R.id.tv_percentage_death);
        TextView tvPercentageRecover = itemView.findViewById(R.id.tv_percentage_recover);
        tvConfirmed.setText(model.getConfirm()+"");
        tvDeaths.setText(model.getDeath()+"");
        tvRecovered.setText(model.getRecover()+"");
        tvCountries.setText(model.getCountry()+"");
        tvPercentageDeath.setText(format.format(model.getPercentageDeath())+"%");
        tvPercentageRecover.setText(format.format(model.getPercentageRecover())+"%");

        View btReload = findViewById(R.id.bt_reload);
        btReload.setOnClickListener(v -> {
            viewLoad.setVisibility(View.VISIBLE);
            CoronaDownloader.get().load(new CoronaDownloader.DownLoadCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        CoronaDataHelper.get().syn(QuickAccessActivity.this);
                        loadData();
                    });

                }

                @Override
                public void onFail() {
                    runOnUiThread(() -> {
                        CoronaDataHelper.get().syn(QuickAccessActivity.this);
                        loadData();
                    });
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tvDate!=null){
            tvDate.setText(new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH).format(new Date()));
        }

    }


    private void clearStatusBarAndNavigationBar() {
        if (getWindow() != null) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {

    }
    private void noStatusBarAndNavigation(){
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }catch (Exception ignored){}

    }
}
