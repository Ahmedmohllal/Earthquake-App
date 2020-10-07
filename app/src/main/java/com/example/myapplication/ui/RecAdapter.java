package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.pojo.properties;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.EarthViewHolder> {
    ArrayList<properties> arrayList = new ArrayList<>();
    onEarthClick onEarthClick;

    public RecAdapter(ArrayList<properties> arrayList, RecAdapter.onEarthClick onEarthClick) {
        this.arrayList = arrayList;
        this.onEarthClick = onEarthClick;
    }

    @NonNull
    @Override
    public RecAdapter.EarthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthitem, parent, false);
        return new EarthViewHolder(view,onEarthClick);

    }

    @Override
    public void onBindViewHolder(@NonNull EarthViewHolder holder, int position) {
        holder.placeview.setText(arrayList.get(position).getPlace());
        holder.magview.setText(arrayList.get(position).getMag() + "");
        holder.urlview.setText(arrayList.get(position).getUrl());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<properties> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public class EarthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView placeview, magview, urlview;
        onEarthClick onEarthClick;

        public EarthViewHolder(@NonNull View itemView, onEarthClick onEarthClick) {
            super(itemView);
            placeview = itemView.findViewById(R.id.placeView);
            magview = itemView.findViewById(R.id.magView);
            urlview = itemView.findViewById(R.id.urlView);

            this.onEarthClick = onEarthClick;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            onEarthClick.getItemClick(getAdapterPosition());

        }
    }
    public interface onEarthClick{
        void getItemClick(int position);
    }
}
