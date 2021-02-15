package com.aarshinkov.mobile.hotelly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;

    public HotelAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hotel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = data.get(position);
        holder.getHotelName().setText(name);
        holder.getCardView().setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), "Test", Toast.LENGTH_SHORT).show();
            Toast.makeText(v.getContext(), data.get(position), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView hotelName;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName);
            cardView = itemView.findViewById(R.id.hotel_card);
        }

        public TextView getHotelName() {
            return hotelName;
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}
