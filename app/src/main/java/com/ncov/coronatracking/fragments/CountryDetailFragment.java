package com.ncov.coronatracking.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.adapters.ProvinceAdapter;
import com.ncov.coronatracking.ads.Callback;
import com.ncov.coronatracking.ads.MyAds;
import com.ncov.coronatracking.helpers.CoronaDataHelper;
import com.ncov.coronatracking.models.ChartData;
import com.ncov.coronatracking.models.CoronaCountry;
import com.ncov.coronatracking.models.Province;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CountryDetailFragment extends BaseFragment {

    private String country;

    public static CountryDetailFragment newInstance() {
        Bundle args = new Bundle();
        CountryDetailFragment fragment = new CountryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CountryDetailFragment set(String country) {
        this.country = country;
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View loadMain = view.findViewById(R.id.view_load_main);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        loadMain.setVisibility(View.VISIBLE);
        tvTitle.setText(country);

        new Handler().postDelayed(()
                -> CoronaDataHelper.get().getCountry(activity, country, (model, provinces) -> {
            getData(model,provinces);
            loadMain.setVisibility(View.GONE);
        }),400);

        view.findViewById(R.id.bt_country_live_map).setOnClickListener(v
                -> MyAds.showInterFull(activity, (value, where)
                -> CountryLiveMapFragment.add(activity,CountryLiveMapFragment.newInstance().set(country))));

        setupLineChart();

    }

    private LineChart chart;
    private void setupLineChart() {
        chart = view.findViewById(R.id.line_chart);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(true);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                float check = (int)value;
                String s =  value%check!=0?"":(int)check+"";
                if(s.length()>3){
                    s = new StringBuilder(s).insert(s.length()-3, ".").toString();
                }
                return s;
            }
        });
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setLabelCount(7,true);

        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        Legend l = chart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        chartData();
    }

    private void chartData() {
        CoronaDataHelper.get().findChartData(charts -> {

            for (ChartData chartData:charts){
                Log.e("hoang",chartData.getDate()+"/"+chartData.getConfirm()+"/"+chartData.getDeaths()+"/"+chartData.getRecover());
            }
            //confirm
            activity.runOnUiThread(() -> {
                chart.resetTracking();
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                ArrayList<Entry> valuesConfirm = new ArrayList<>();
                for (int i=0;i<charts.size();i++){
                    valuesConfirm.add(new Entry(i,charts.get(i).getConfirm()));
                }
                LineDataSet a = new LineDataSet(valuesConfirm, "Confirmed");
                a.setLineWidth(2.5f);
                a.setCircleRadius(4f);
                a.setColor(Color.parseColor("#DD2C00"));
                a.setCircleColor(Color.parseColor("#DD2C00"));
                a.setCircleHoleColor(Color.WHITE);
                dataSets.add(a);

                ArrayList<Entry> valuesDeaths= new ArrayList<>();
                for (int i=0;i<charts.size();i++){
                    valuesDeaths.add(new Entry(i,charts.get(i).getDeaths()));
                }
                LineDataSet b = new LineDataSet(valuesDeaths, "Deaths");
                b.setLineWidth(2.5f);
                b.setCircleRadius(4f);
                b.setColor(Color.parseColor("#74ECECEC"));
                b.setCircleColor(Color.parseColor("#74ECECEC"));
                dataSets.add(b);

                ArrayList<Entry> valuesRecover= new ArrayList<>();
                for (int i=0;i<charts.size();i++){
                    valuesRecover.add(new Entry(i,charts.get(i).getRecover()));
                }
                LineDataSet c = new LineDataSet(valuesRecover, "Recovered");
                c.setLineWidth(2.5f);
                c.setCircleRadius(4f);
                c.setColor(Color.parseColor("#00BB4D"));
                c.setCircleColor(Color.parseColor("#00BB4D"));
                dataSets.add(c);


                LineData data = new LineData(dataSets);
                data.setValueTextColor(Color.WHITE);
                data.setDrawValues(false);
                chart.setData(data);
                chart.invalidate();

                chart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return charts.get((int)value).getDate().replace("/20","");
                    }
                });
            });
        },country);

    }

    @SuppressLint("SetTextI18n")
    private void getData(CoronaCountry model, ArrayList<Province> provinces) {
        if(!isAdded()){
            return;
        }
        DecimalFormat format = new DecimalFormat("#.##");
        TextView tvConfirmed = view.findViewById(R.id.tv_confirmed);
        TextView tvDeaths= view.findViewById(R.id.tv_deaths);
        TextView tvRecovered = view.findViewById(R.id.tv_recovered);
        TextView tvProvince = view.findViewById(R.id.tv_province );
        TextView tvPercentageDeath = view.findViewById(R.id.tv_percentage_death);
        TextView tvPercentageRecover = view.findViewById(R.id.tv_percentage_recover);

        tvConfirmed.setText(model.getConfirm()+"");
        tvDeaths.setText(model.getDeath()+"");
        tvRecovered.setText(model.getRecover()+"");
        tvProvince.setText(model.getProvince()+"");
        tvPercentageDeath.setText(format.format(model.getPercentageDeath())+"%");
        tvPercentageRecover.setText(format.format(model.getPercentageRecover())+"%");


        RecyclerView rcvProvince = view.findViewById(R.id.rcv_provinces);
        rcvProvince.setLayoutManager(new LinearLayoutManager(activity));
        rcvProvince.setAdapter(new ProvinceAdapter(activity,provinces));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_country_detail;
    }
}
