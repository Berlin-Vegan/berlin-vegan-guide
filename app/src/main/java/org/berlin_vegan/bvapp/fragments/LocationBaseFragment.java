package org.berlin_vegan.bvapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.berlin_vegan.bvapp.activities.LocationDetailActivity;
import org.berlin_vegan.bvapp.data.Location;

public class LocationBaseFragment extends Fragment {

    Location initLocation(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                return (Location) extras.getSerializable(LocationDetailActivity.EXTRA_LOCATION);
            }
        } else {
            return (Location) savedInstanceState.getSerializable(LocationDetailActivity.EXTRA_LOCATION);
        }
        return null;
    }
}
