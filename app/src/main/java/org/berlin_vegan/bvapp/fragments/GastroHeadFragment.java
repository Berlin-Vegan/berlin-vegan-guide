package org.berlin_vegan.bvapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.helpers.UiUtils;


public class GastroHeadFragment extends Fragment {
    private GastroLocation mGastroLocation;

    public GastroHeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gastro_head_fragment, container, false);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                mGastroLocation = (GastroLocation) extras.getSerializable(GastroActivity.EXTRA_GASTRO_LOCATION);
            }
        } else {
            mGastroLocation = (GastroLocation) savedInstanceState.getSerializable(GastroActivity.EXTRA_GASTRO_LOCATION);
        }
        final TextView titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        titleTextView.setText(mGastroLocation.getName());
        final TextView streetTextView = (TextView) view.findViewById(R.id.text_view_street);
        streetTextView.setText(mGastroLocation.getStreet());

        final TextView distanceTextView = (TextView) view.findViewById(R.id.text_view_distance);
        final Float distToCurLoc = mGastroLocation.getDistToCurLoc();
        if (distToCurLoc > -1.0f) {
            distanceTextView.setText(UiUtils.getFormattedDistance(distToCurLoc, getActivity()));
        } else {
            distanceTextView.setText("");
        }
        return view;
    }

}
