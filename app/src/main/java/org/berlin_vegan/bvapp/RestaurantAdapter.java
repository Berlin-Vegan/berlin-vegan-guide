package org.berlin_vegan.bvapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.view.View.OnClickListener;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<RestaurantInfo> restaurantInfoList;

    public RestaurantAdapter(List<RestaurantInfo> restaurantInfoList) {
        this.restaurantInfoList = restaurantInfoList;
    }

    @Override
    public int getItemCount() {
        return restaurantInfoList.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i) {
        RestaurantInfo restaurantInfo = restaurantInfoList.get(i);
        restaurantViewHolder.vTitle.setText(restaurantInfo.name);
        restaurantViewHolder.vName.setText(restaurantInfo.name);
        restaurantViewHolder.vAddress.setText(restaurantInfo.address);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new RestaurantViewHolder(itemView);
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView vTitle;
        protected TextView vName;
        protected TextView vAddress;

        public RestaurantViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            vTitle = (TextView) v.findViewById(R.id.title);
            vName = (TextView) v.findViewById(R.id.text_view_name);
            vAddress = (TextView) v.findViewById(R.id.text_view_address);
        }

        @Override
        public void onClick(View view) {
            // TODO: open new view with detailed restaurant description
            Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
