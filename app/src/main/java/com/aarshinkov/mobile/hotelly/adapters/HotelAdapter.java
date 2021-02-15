package com.aarshinkov.mobile.hotelly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

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

        // The layout wrapper
        private final CardView cardView;
        private final TextView hotelName;
        private final ImageView hotelImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.hotel_card);

            hotelName = itemView.findViewById(R.id.hotelName);
            hotelImage = itemView.findViewById(R.id.hotelImage);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getHotelName() {
            return hotelName;
        }

        public ImageView getHotelImage() {
            return hotelImage;
        }
    }
}
