package org.berlin_vegan.bvapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.view.View.OnClickListener;

public class GastroLocationAdapter extends RecyclerView.Adapter<GastroLocationAdapter.GastroLocationViewHolder> {

    private static List<GastroLocation> mGastroLocationList;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public GastroLocationAdapter(Context context, List<GastroLocation> gastroLocationList) {
        mGastroLocationList = gastroLocationList;
        mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public int getItemCount() {
        return mGastroLocationList.size();
    }

    @Override
    public void onBindViewHolder(GastroLocationViewHolder gastroLocationViewHolder, int i) {
        GastroLocation gastroLocation = mGastroLocationList.get(i);
        gastroLocationViewHolder.vTitle.setText(gastroLocation.getName());
        gastroLocationViewHolder.vStreet.setText(gastroLocation.getStreet());
        float distToCurLoc = gastroLocation.getDistToCurLoc();
        if (distToCurLoc > -1.0f) {
            // TODO: speed up reloading the distances after a settings change
            // string for distance unit depends on settings
            String distance = String.valueOf(distToCurLoc) + " ";
            if (sharedPreferences.getBoolean("key_units", true)) {
                distance += mContext.getString(R.string.km_string);
            } else {
                distance += mContext.getString(R.string.mi_string);
            }
            gastroLocationViewHolder.vDistance.setText(distance);
        }
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
        protected TextView vStreet;
        protected TextView vDistance;

        public GastroLocationViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            vTitle = (TextView) v.findViewById(R.id.title);
            vStreet = (TextView) v.findViewById(R.id.text_view_street);
            vDistance = (TextView) v.findViewById(R.id.text_view_distance);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, GastroActivity.class);
            int position = getAdapterPosition();
            GastroLocation gastroLocation = mGastroLocationList.get(position);
            intent.putExtra("GASTRO_LOCATION", gastroLocation);
            context.startActivity(intent);
        }
    }
}
