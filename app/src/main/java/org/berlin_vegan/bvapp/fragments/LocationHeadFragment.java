package org.berlin_vegan.bvapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.LocationDetailActivity;
import org.berlin_vegan.bvapp.data.Location;
import org.berlin_vegan.bvapp.helpers.UiUtils;


public class LocationHeadFragment extends LocationBaseFragment {
    private Location mLocation;

    public LocationHeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_head_fragment, container, false);
        mLocation = initLocation(savedInstanceState);
        final TextView titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        titleTextView.setText(mLocation.getName());
        final TextView streetTextView = (TextView) view.findViewById(R.id.text_view_street);
        streetTextView.setText(mLocation.getStreet());

        final TextView distanceTextView = (TextView) view.findViewById(R.id.text_view_distance);
        final Float distToCurLoc = mLocation.getDistToCurLoc();
        if (distToCurLoc > -1.0f) {
            distanceTextView.setText(UiUtils.getFormattedDistance(distToCurLoc, getActivity()));
        } else {
            distanceTextView.setText("");
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(LocationDetailActivity.EXTRA_LOCATION, mLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

}
