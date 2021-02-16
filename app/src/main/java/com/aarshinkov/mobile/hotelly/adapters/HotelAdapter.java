package com.aarshinkov.mobile.hotelly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;
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
        holder.getHotelName().setText(name);

        String imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
        Picasso.get().load(imageUrl).into(holder.getHotelImage());

        holder.getHotelStars().setRating(20);
        holder.getHotelStars().setNumStars(hotel.getStars());
        holder.getHotelCity().setText(hotel.getAddress().getCity());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String strDate = formatter.format(hotel.getCreatedOn());
        holder.getHotelCreatedOn().setText(strDate);

        holder.getCardView().setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), RootActivity.class);
//            v.getContext().startActivity(intent);
            Toast.makeText(v.getContext(), hotel.getHotelId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The layout wrapper
        private final CardView cardView;
        private final ImageView hotelImage;
        private final TextView hotelName;
        private final RatingBar hotelStars;
        private final TextView hotelCity;
        private final TextView hotelCreatedOn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.hotel_card);

            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelStars = itemView.findViewById(R.id.hotelStars);
            hotelCity = itemView.findViewById(R.id.hotelCity);
            hotelCreatedOn = itemView.findViewById(R.id.hotelCreatedOn);
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getHotelImage() {
            return hotelImage;
        }

        public TextView getHotelName() {
            return hotelName;
        }

        public RatingBar getHotelStars() {
            return hotelStars;
        }

        public TextView getHotelCity() {
            return hotelCity;
        }

        public TextView getHotelCreatedOn() {
            return hotelCreatedOn;
        }
    }
}
