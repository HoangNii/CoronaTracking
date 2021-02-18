package com.ncov.coronatracking.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.helpers.CoronaDataHelper;
import com.ncov.coronatracking.models.CoronaMaker;

import java.util.ArrayList;

public class LiveMapFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MapView mMapView;

    public static LiveMapFragment newInstance() {
        Bundle args = new Bundle();
        LiveMapFragment fragment = new LiveMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;

            ArrayList<CoronaMaker> makers = CoronaDataHelper.get().findCoronaMakers(activity);
            if(makers.size()>0){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(makers.get(0).getLat(),makers.get(0).getLon()), 1f), 2000, null);
            }
            for (CoronaMaker coronaMaker: makers){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(coronaMaker.getLat(),coronaMaker.getLon()));
                String title = TextUtils.isEmpty(coronaMaker.getProvince())?coronaMaker.getCountry():
                        coronaMaker.getProvince()+" - "+coronaMaker.getCountry();
                markerOptions.title(title);
                markerOptions.snippet("Confirmed: "+coronaMaker.getConfirm()
                +" - Deaths: "+coronaMaker.getDeath()+" - Recovered: "+coronaMaker.getRecover());
                mMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMapView(savedInstanceState);
    }

    private void initMapView(Bundle bd){
        mMapView =  view.findViewById(R.id.map);
        mMapView.onCreate(bd);
        new Handler().postDelayed(() -> {
            if(isAdded()&&mMapView!=null){
                mMapView.getMapAsync(LiveMapFragment.this);
            }
        },400);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_map;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


}
