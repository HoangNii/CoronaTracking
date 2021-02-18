package com.ncov.coronatracking.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ncov.coronatracking.adapters.CountryAdapter;
import com.ncov.coronatracking.ads.Callback;
import com.ncov.coronatracking.ads.MyAds;
import com.ncov.coronatracking.helpers.CoronaDataHelper;
import com.ncov.coronatracking.models.ChartData;
import com.ncov.coronatracking.models.Country;
import com.ncov.coronatracking.models.CoronaMain;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends BaseFragment {

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View loadMain = view.findViewById(R.id.view_load_main);
        loadMain.setVisibility(View.VISIBLE);

        TextView tvTime = view.findViewById(R.id.tv_time);
        tvTime.setText("Update "+new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH).format(new Date()));

        CoronaDataHelper.get().getMain(activity, (model, coronaCountryModels) -> {
            getData(model,coronaCountryModels);
            loadMain.setVisibility(View.GONE);
        });

        setupLineChart();
        MyAds.initNativeMain(view.findViewById(R.id.adLayout));

    }

    @SuppressLint("SetTextI18n")
    private void getData(CoronaMain model, ArrayList<Country> countries) {
        DecimalFormat format = new DecimalFormat("#.##");
        TextView tvConfirmed = view.findViewById(R.id.tv_confirmed);
        TextView tvDeaths= view.findViewById(R.id.tv_deaths);
        TextView tvRecovered = view.findViewById(R.id.tv_recovered);
        TextView tvCountries = view.findViewById(R.id.tv_country);
        TextView tvPercentageDeath = view.findViewById(R.id.tv_percentage_death);
        TextView tvPercentageRecover = view.findViewById(R.id.tv_percentage_recover);
        tvConfirmed.setText(model.getConfirm()+"");
        tvDeaths.setText(model.getDeath()+"");
        tvRecovered.setText(model.getRecover()+"");
        tvCountries.setText(model.getCountry()+"");
        tvPercentageDeath.setText(format.format(model.getPercentageDeath())+"%");
        tvPercentageRecover.setText(format.format(model.getPercentageRecover())+"%");

        RecyclerView rcvCountry = view.findViewById(R.id.rcv_countries);
        rcvCountry.setNestedScrollingEnabled(false);
        rcvCountry.setLayoutManager(new LinearLayoutManager(activity));
        rcvCountry.setAdapter(new CountryAdapter(activity, countries) {
            @Override
            public void OnItemClick(Country country) {
                MyAds.showInterFull(activity, (value, where)
                        -> CountryDetailFragment.add(activity, CountryDetailFragment.newInstance()
                        .set(country.getName())));

            }
        });

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
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setLabelCount(8, true);

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
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }


}
