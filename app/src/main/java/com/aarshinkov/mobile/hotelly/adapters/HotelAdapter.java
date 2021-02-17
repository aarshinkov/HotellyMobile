package com.aarshinkov.mobile.hotelly.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.HotelActivity;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.aarshinkov.mobile.hotelly.utils.Constants.BASE_URL;

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
        holder.getHotelsNameTV().setText(name);

        String imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
        Picasso.get().load(imageUrl).into(holder.getHotelsImageIV());

        holder.getHotelsStarsRB().setRating(20);
        holder.getHotelsStarsRB().setNumStars(hotel.getStars());
        holder.getHotelsCityTV().setText(hotel.getAddress().getCity());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String strDate = formatter.format(hotel.getCreatedOn());
        holder.getHotelsCreatedOnTV().setText(strDate);

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HotelActivity.class);
            intent.putExtra("hotelId", hotel.getHotelId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The layout wrapper
        private final CardView cardView;
        private final ImageView hotelsImageIV;
        private final TextView hotelsNameTV;
        private final RatingBar hotelsStarsRB;
        private final TextView hotelsCityTV;
        private final TextView hotelsCreatedOnTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.hotel_card);

            hotelsImageIV = itemView.findViewById(R.id.hotelsImageIV);
            hotelsNameTV = itemView.findViewById(R.id.hotelsNameTV);
            hotelsStarsRB = itemView.findViewById(R.id.hotelsStarsRB);
            hotelsCityTV = itemView.findViewById(R.id.hotelsCityTV);
            hotelsCreatedOnTV = itemView.findViewById(R.id.hotelsCreatedOnTV);
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getHotelsImageIV() {
            return hotelsImageIV;
        }

        public TextView getHotelsNameTV() {
            return hotelsNameTV;
        }

        public RatingBar getHotelsStarsRB() {
            return hotelsStarsRB;
        }

        public TextView getHotelsCityTV() {
            return hotelsCityTV;
        }

        public TextView getHotelsCreatedOnTV() {
            return hotelsCreatedOnTV;
        }
    }
}
