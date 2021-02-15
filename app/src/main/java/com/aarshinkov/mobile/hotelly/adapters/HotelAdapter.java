package com.aarshinkov.mobile.hotelly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<HotelGetResponse> data;

    public HotelAdapter(Context context, List<HotelGetResponse> data) {
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

        HotelGetResponse hotel = data.get(position);
        String name = hotel.getName();
        holder.getHotelName().setText(name);
        holder.getCardView().setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), RootActivity.class);
//            v.getContext().startActivity(intent);
            Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView hotelName;
        private final CardView cardView;

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
