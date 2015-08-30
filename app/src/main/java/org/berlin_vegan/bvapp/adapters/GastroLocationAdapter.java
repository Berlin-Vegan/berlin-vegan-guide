package org.berlin_vegan.bvapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.helpers.DateUtil;
import org.berlin_vegan.bvapp.helpers.UiUtils;

import java.util.Date;
import java.util.GregorianCalendar;

import static android.view.View.OnClickListener;

/**
 * Fills the {@code RecyclerView} of {@link MainListActivity} with its content.
 */
public class GastroLocationAdapter extends RecyclerView.Adapter<GastroLocationAdapter.GastroLocationViewHolder> {

    private final MainListActivity mMainListActivity;

    public GastroLocationAdapter(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
    }

    @Override
    public int getItemCount() {
        return mMainListActivity.getGastroLocations().size();
    }

    @Override
    public void onBindViewHolder(GastroLocationViewHolder viewHolder, int i) {
        final GastroLocation gastroLocation = mMainListActivity.getGastroLocations().get(i);
        viewHolder.vTitle.setText(gastroLocation.getName());
        viewHolder.vStreet.setText(gastroLocation.getStreet());
        final float distToCurLoc = gastroLocation.getDistToCurLoc();
        if (distToCurLoc > -1.0f) {
            // TODO: speed up reloading the distances after a settings change
            // string for distance unit depends on settings
            final String distance = UiUtils.getFormattedDistance(distToCurLoc, mMainListActivity);
            viewHolder.vDistance.setText(distance);
        }
        // update opening hours field
        final Date currentTime = GregorianCalendar.getInstance().getTime();
        final Date currentTimePlus30Minutes = DateUtil.addMinutesToDate(currentTime, 30);
        if (!gastroLocation.isOpen(currentTime)) {
            viewHolder.vClosed.setText(mMainListActivity.getString(R.string.gastro_list_closed));
            viewHolder.vClosed.setTextColor(mMainListActivity.getResources().getColor(R.color.disabled));
            viewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.disabled));
        }else if (!gastroLocation.isOpen(currentTimePlus30Minutes)) {
            final String formattedClosingTime = gastroLocation.getFormattedClosingTime(currentTime);
            viewHolder.vClosed.setText(mMainListActivity.getString(R.string.gastro_list_closed_soon, formattedClosingTime));
            viewHolder.vClosed.setTextColor(mMainListActivity.getResources().getColor(R.color.text_attention));
            viewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.theme_primary));
        }else {
            viewHolder.vClosed.setText(""); // clear
            viewHolder.vDistance.setTextColor(mMainListActivity.getResources().getColor(R.color.theme_primary));
        }

        // update vegan label, indicate 100% vegan locations
        if (gastroLocation.getVegan() == GastroLocation.VEGAN) {
            viewHolder.vVeganLabel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vVeganLabel.setVisibility(View.INVISIBLE);
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
        final ImageView vVeganLabel;

        public GastroLocationViewHolder(View v, MainListActivity mainListActivity) {
            super(v);
            v.setOnClickListener(this);
            mMainListActivity = mainListActivity;
            vTitle = (TextView) v.findViewById(R.id.text_view_title);
            vVeganLabel = (ImageView) v.findViewById(R.id.image_view_vegan_label);
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
