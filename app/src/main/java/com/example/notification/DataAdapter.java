package com.example.notification;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private List<DataItem> dataItems;
    private Set<Integer> expandedPositions;

    public DataAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.expandedPositions = new HashSet<>();
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_data, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataItem currentItem = dataItems.get(position);

        holder.temperatureTextView.setText("Temperature: " + currentItem.getTemperature() + " °C");
        holder.humidityTextView.setText("Humidity: " + currentItem.getHumidity() + " %");
        holder.heartBeatTextView.setText("Heart Beat: " + currentItem.getHeartBeat() + " bpm");

        // Xử lý sự kiện bấm vào phần tử
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandedPositions.contains(position)) {
                    expandedPositions.remove(position);
                } else {
                    expandedPositions.add(position);
                }
                notifyDataSetChanged();
            }
        });

        // Cập nhật trạng thái của ViewHolder dựa trên expandedPositions
        if (expandedPositions.contains(position)) {
            holder.showDetails();
        } else {
            holder.hideDetails();
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView temperatureTextView;
        public TextView humidityTextView;
        public TextView heartBeatTextView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTextView = itemView.findViewById(R.id.item_temperature);
            humidityTextView = itemView.findViewById(R.id.item_humidity);
            heartBeatTextView = itemView.findViewById(R.id.item_heart_beat);
        }

        public void showDetails() {
            // Hiển thị dữ liệu chi tiết (nếu có)
            temperatureTextView.setVisibility(View.VISIBLE);
            humidityTextView.setVisibility(View.VISIBLE);
            heartBeatTextView.setVisibility(View.VISIBLE);
        }

        public void hideDetails() {
            // Ẩn dữ liệu chi tiết (nếu có)
            temperatureTextView.setVisibility(View.GONE);
            humidityTextView.setVisibility(View.GONE);
            heartBeatTextView.setVisibility(View.GONE);
        }
    }
}

