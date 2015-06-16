package org.berlin_vegan.bvapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.GastroLocations;

import static android.view.View.OnClickListener;

/**
 * Fills the {@code RecyclerView} of {@link MainListActivity} with its content.
 */
public class GastroLocationAdapter extends RecyclerView.Adapter<GastroLocationAdapter.GastroLocationViewHolder> {

    private final MainListActivity mMainListActivity;
    private final SharedPreferences mSharedPreferences;

    public GastroLocationAdapter(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mMainListActivity);
    }

    @Override
    public int getItemCount() {
        return mMainListActivity.getGastroLocations().size();
    }

    @Override
    public void onBindViewHolder(GastroLocationViewHolder gastroLocationViewHolder, int i) {
        GastroLocation gastroLocation = mMainListActivity.getGastroLocations().get(i);
        StringBuilder title = new StringBuilder()
                .append(gastroLocation.getName())
                .append(" ")
                .append("(")
                .append(gastroLocation.getDistrict())
                .append(")");
        gastroLocationViewHolder.vTitle.setText(title);
        gastroLocationViewHolder.vStreet.setText(gastroLocation.getStreet());
        float distToCurLoc = gastroLocation.getDistToCurLoc();
        if (distToCurLoc > -1.0f) {
            // TODO: speed up reloading the distances after a settings change
            // string for distance unit depends on settings
            String distance = String.valueOf(distToCurLoc) + " ";
            if (mSharedPreferences.getBoolean(GastroLocations.KEY_UNITS, true)) {
                distance += mMainListActivity.getString(R.string.km_string);
            } else {
                distance += mMainListActivity.getString(R.string.mi_string);
            }
            gastroLocationViewHolder.vDistance.setText(distance);
        }
    }

    @Override
    public GastroLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.main_list_card_view, viewGroup, false);

        return new GastroLocationViewHolder(itemView, mMainListActivity);
    }

    public static class GastroLocationViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final MainListActivity mMainListActivity;
        final TextView vTitle;
        final TextView vStreet;
        final TextView vDistance;

        public GastroLocationViewHolder(View v, MainListActivity mainListActivity) {
            super(v);
            v.setOnClickListener(this);
            mMainListActivity = mainListActivity;
            vTitle = (TextView) v.findViewById(R.id.text_view_title);
            vStreet = (TextView) v.findViewById(R.id.text_view_street);
            vDistance = (TextView) v.findViewById(R.id.text_view_distance);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, GastroActivity.class);
            int position = getAdapterPosition();
            GastroLocation gastroLocation = mMainListActivity.getGastroLocations().get(position);
            intent.putExtra("GASTRO_LOCATION", gastroLocation);
            context.startActivity(intent);
        }
    }
}
