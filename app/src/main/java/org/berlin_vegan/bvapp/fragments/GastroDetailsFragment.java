package org.berlin_vegan.bvapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;

/**
 * Holds content for the details tab in {@link org.berlin_vegan.bvapp.activities.GastroActivity}.
 */
public class GastroDetailsFragment extends Fragment {

    private GastroLocation mGastroLocation;

    final public static int OMNIVORE_VEGAN_DECLARED = 2;
    final public static int VEGETARIAN_VEGAN_DECLARED = 4;
    final public static int VEGAN = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gastro_details_fragment, container, false);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                mGastroLocation = (GastroLocation) extras.getSerializable(GastroActivity.EXTRA_GASTRO_LOCATION);
            }
        } else {
            mGastroLocation = (GastroLocation) savedInstanceState.getSerializable(GastroActivity.EXTRA_GASTRO_LOCATION);
        }

        addAddress(v);

        return v;
    }

    private void addAddress(final View v) {
        final RelativeLayout item = (RelativeLayout) v.findViewById(R.id.address);

        final ImageView icon = (ImageView) item.findViewById(R.id.icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_place_white_24dp));
        icon.setColorFilter(getResources().getColor(R.color.theme_primary));

        final String text;
        final String street = mGastroLocation.getStreet();
        final int cityCode = mGastroLocation.getCityCode();
        final String city = mGastroLocation.getCity();
        if (street != null) {
            text = "<a href=\"http://maps.google.com/maps?q="
                    // google maps
                    + street + ", " + cityCode + ", " + city + "\">"
                    // view
                    + street + ", " + cityCode + " " + city + "</a>";
        } else {
            text = getString(R.string.gastro_details_contact_address);
        }
        final TextView content = (TextView) item.findViewById(R.id.content);
        content.setText(Html.fromHtml(text));
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(GastroActivity.EXTRA_GASTRO_LOCATION, mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
