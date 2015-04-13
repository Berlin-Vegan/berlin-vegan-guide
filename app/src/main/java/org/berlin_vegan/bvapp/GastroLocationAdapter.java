package org.berlin_vegan.bvapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.view.View.OnClickListener;

public class GastroLocationAdapter extends RecyclerView.Adapter<GastroLocationAdapter.GastroLocationViewHolder> {

    private List<GastroLocation> gastroLocationList;

    public GastroLocationAdapter(List<GastroLocation> gastroLocationList) {
        this.gastroLocationList = gastroLocationList;
    }

    @Override
    public int getItemCount() {
        return gastroLocationList.size();
    }

    @Override
    public void onBindViewHolder(GastroLocationViewHolder gastroLocationViewHolder, int i) {
        GastroLocation gastroLocation = gastroLocationList.get(i);
        gastroLocationViewHolder.vTitle.setText(gastroLocation.getName());
        gastroLocationViewHolder.vName.setText(gastroLocation.getName());
        gastroLocationViewHolder.vStreet.setText(gastroLocation.getStreet());
    }

    @Override
    public GastroLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new GastroLocationViewHolder(itemView);
    }

    public static class GastroLocationViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView vTitle;
        protected TextView vName;
        protected TextView vStreet;

        public GastroLocationViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            vTitle = (TextView) v.findViewById(R.id.title);
            vName = (TextView) v.findViewById(R.id.text_view_name);
            vStreet = (TextView) v.findViewById(R.id.text_view_street);
        }

        @Override
        public void onClick(View view) {
            // TODO: open new view with detailed gastronomy description
            Toast.makeText(view.getContext(), "position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
