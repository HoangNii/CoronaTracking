package com.ncov.coronatracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ncov.coronatracking.App;
import com.ncov.coronatracking.activities.MainActivity;
import com.ncov.coronatracking.R;

public abstract class BaseFragment extends DialogFragment {

    protected View view;

    protected MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        App.get().sendTracker(getClass().getSimpleName());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        View btnBack = view.findViewById(R.id.bt_back);
        if(btnBack!=null){
            btnBack.setOnClickListener(v -> activity.onBackPressed());
        }
        return view;
    }

    protected abstract int getLayoutId();

    public static void add(AppCompatActivity activity, Fragment fragment){
        if(activity.getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName())==null){
            addFragment(activity,fragment,fragment.getClass().getName());
        }
    }


    private static void addFragment(AppCompatActivity activity, @NonNull Fragment fragment,
                                    @NonNull String tag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in,R.anim.anim_out, R.anim.anim_in,R.anim.anim_out)
                .addToBackStack(tag)
                .add(R.id.main, fragment, tag)
                .commitAllowingStateLoss();

    }


}
