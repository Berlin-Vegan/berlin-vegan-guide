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

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.GastroLocations;
import org.berlin_vegan.bvapp.helpers.DateUtil;

import java.util.Date;
import java.util.GregorianCalendar;

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
        final GastroLocation gastroLocation = mMainListActivity.getGastroLocations().get(i);
        gastroLocationViewHolder.vTitle.setText(gastroLocation.getName());
        gastroLocationViewHolder.vStreet.setText(gastroLocation.getStreet());
        final float distToCurLoc = gastroLocation.getDistToCurLoc();
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
        // update opening hours field
        final Date currentTime = GregorianCalendar.getInstance().getTime();
        final Date currentTimePlus30Minutes = DateUtil.addMinutesToDate(currentTime, 30);
        if (!gastroLocation.isOpen(currentTime)) {
            gastroLocationViewHolder.vClosed.setText(mMainListActivity.getString(R.string.gastro_list_closed));
            gastroLocationViewHolder.vClosed.setTextColor(mMainListActivity.getResources().getColor(R.color.text_disabled));
            gastroLocationViewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.text_disabled));
        }else if (!gastroLocation.isOpen(currentTimePlus30Minutes)) {
            final String formattedClosingTime = gastroLocation.getFormattedClosingTime(currentTime);
            gastroLocationViewHolder.vClosed.setText(mMainListActivity.getString(R.string.gastro_list_closed_soon, formattedClosingTime));
            gastroLocationViewHolder.vClosed.setTextColor(mMainListActivity.getResources().getColor(R.color.text_attention));
            gastroLocationViewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.theme_primary));
        }else {
            gastroLocationViewHolder.vClosed.setText(""); // clear
            gastroLocationViewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.theme_primary));
        }
    }

    @Override
    public GastroLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.main_list_item, viewGroup, false);

        return new GastroLocationViewHolder(itemView, mMainListActivity);
    }

    public static class GastroLocationViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final MainListActivity mMainListActivity;
        final TextView vTitle;
        final TextView vClosed;
        final TextView vStreet;
        final TextView vDistance;

        public GastroLocationViewHolder(View v, MainListActivity mainListActivity) {
            super(v);
            v.setOnClickListener(this);
            mMainListActivity = mainListActivity;
            vTitle = (TextView) v.findViewById(R.id.text_view_title);
            vClosed = (TextView) v.findViewById(R.id.text_view_closed);
            vStreet = (TextView) v.findViewById(R.id.text_view_street);
            vDistance = (TextView) v.findViewById(R.id.text_view_distance);
        }

        @Override
        public void onClick(View view) {
            final Context context = view.getContext();
            final Intent intent = new Intent(context, GastroActivity.class);
            final int position = getAdapterPosition();
            final GastroLocation gastroLocation = mMainListActivity.getGastroLocations().get(position);
            intent.putExtra(GastroActivity.EXTRA_GASTRO_LOCATION, gastroLocation);
            context.startActivity(intent);
        }
    }
}
