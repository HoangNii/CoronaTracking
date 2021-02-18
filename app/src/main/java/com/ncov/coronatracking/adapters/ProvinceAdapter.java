package com.ncov.coronatracking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ncov.coronatracking.R;
import com.ncov.coronatracking.models.Country;
import com.ncov.coronatracking.models.Province;

import java.util.ArrayList;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {

    private final Context context;

    private final ArrayList<Province> provinces;

    public ProvinceAdapter(Context context, ArrayList<Province> provinces) {
        this.context = context;
        this.provinces = provinces;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_province,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Province province = provinces.get(position);
        holder.tvProvince.setText(province.getName().length()>0?province.getName():"???");
        holder.tvConfirm.setText(province.getConfirm()+"");
        holder.tvDeath.setText(province.getDeath()+"");
        holder.tvRecover.setText(province.getRecover()+"");
    }

    @Override
    public int getItemCount() {
        return provinces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvProvince;
        private final TextView tvConfirm;
        private final TextView tvDeath;
        private final TextView tvRecover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProvince = itemView.findViewById(R.id.tv_province);
            tvConfirm = itemView.findViewById(R.id.tv_confirm);
            tvDeath = itemView.findViewById(R.id.tv_death);
            tvRecover = itemView.findViewById(R.id.tv_recover);

        }

    }

}
