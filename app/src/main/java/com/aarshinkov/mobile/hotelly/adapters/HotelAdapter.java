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
import com.aarshinkov.mobile.hotelly.activities.hotels.HotelActivity;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.aarshinkov.mobile.hotelly.utils.Constants.BASE_URL;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final List<HotelGetResponse> hotels;

    public HotelAdapter(Context context, List<HotelGetResponse> hotels) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hotel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        HotelGetResponse hotel = hotels.get(position);

        holder.getHotelItemNameTV().setText(hotel.getName());

        String imageUrl;
        if (hotel.getMainImage() == null) {
            imageUrl = BASE_URL + "images/hotels/unknown.jpg";
        } else {
            imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
        }
        Picasso.get().load(imageUrl).into(holder.getHotelItemImageIV());

        holder.getHotelItemStarsRB().setRating(20);
        holder.getHotelItemStarsRB().setNumStars(hotel.getStars());
        holder.getHotelItemCityTV().setText(hotel.getCity());

        StringBuilder builder = new StringBuilder();
        builder.append(hotel.getStreet()).append(" ");
        builder.append(hotel.getNumber()).append(", ");
        builder.append(Utils.getStringResource(context, "country_" + hotel.getCountryCode()));

        String addressText = String.valueOf(builder);

        holder.getHotelItemAddressTV().setText(addressText);

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HotelActivity.class);
            intent.putExtra("hotelId", hotel.getHotelId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The layout wrapper
        private final CardView cardView;
        private final ImageView hotelItemImageIV;
        private final TextView hotelItemNameTV;
        private final RatingBar hotelItemStarsRB;
        private final TextView hotelItemCityTV;
        private final TextView hotelItemAddressTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.hotel_card);

            hotelItemImageIV = itemView.findViewById(R.id.hotelItemImageIV);
            hotelItemNameTV = itemView.findViewById(R.id.hotelItemNameTV);
            hotelItemStarsRB = itemView.findViewById(R.id.hotelItemStarsRB);
            hotelItemCityTV = itemView.findViewById(R.id.hotelItemCityTV);
            hotelItemAddressTV = itemView.findViewById(R.id.hotelItemAddressTV);
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getHotelItemImageIV() {
            return hotelItemImageIV;
        }

        public TextView getHotelItemNameTV() {
            return hotelItemNameTV;
        }

        public RatingBar getHotelItemStarsRB() {
            return hotelItemStarsRB;
        }

        public TextView getHotelItemCityTV() {
            return hotelItemCityTV;
        }

        public TextView getHotelItemAddressTV() {
            return hotelItemAddressTV;
        }
    }
}
