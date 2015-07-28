package org.berlin_vegan.bvapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.data.GastroLocation;

/**
 * alternative implementation for google maps (play services), at the moment it use google static maps
 * but should use OSM Maps like mapsforge or similar
 */
public class GastroMapFragment extends Fragment {
    private GastroLocation mLocation;

    public GastroMapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // gastro_map_fragment.xml contains just an ImageView
        return inflater.inflate(R.layout.gastro_map_fragment, container, false);
    }

    public void setLocation(GastroLocation location) {
        this.mLocation = location;
        final View view = getView();
        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            StringBuilder url = new StringBuilder();
            url.append("https://maps.googleapis.com/maps/api/staticmap?zoom=17&size=600x500&maptype=roadmap");
            url.append("&scale=2"); // high detail
            url.append("&markers=color:green%7Clabel:G%7C");  // %7C is |
            url.append(mLocation.getLatCoord()).append(",").append(mLocation.getLongCoord());
            Picasso.with(getActivity())
                    .load(url.toString())
                    .into(imageView);
        }
    }
}
