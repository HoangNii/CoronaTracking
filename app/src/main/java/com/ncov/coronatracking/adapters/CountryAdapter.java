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
import java.util.ArrayList;

public abstract class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private final Context context;

    private final ArrayList<Country> countryModels;

    public CountryAdapter(Context context, ArrayList<Country> countryModels) {
        this.context = context;
        this.countryModels = countryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country countryModel = countryModels.get(position);
        holder.tvCountry.setText(countryModel.getName());
        holder.tvConfirm.setText(countryModel.getConfirm()+"");
        holder.tvDeath.setText(countryModel.getDeath()+"");
        holder.tvRecover.setText(countryModel.getRecover()+"");
    }

    @Override
    public int getItemCount() {
        return countryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCountry;
        private final TextView tvConfirm;
        private final TextView tvDeath;
        private final TextView tvRecover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCountry = itemView.findViewById(R.id.tv_country);
            tvConfirm = itemView.findViewById(R.id.tv_confirm);
            tvDeath = itemView.findViewById(R.id.tv_death);
            tvRecover = itemView.findViewById(R.id.tv_recover);

            itemView.setOnClickListener(v -> OnItemClick(countryModels.get(getAdapterPosition())));

        }

    }

    public abstract void OnItemClick(Country country);
}
