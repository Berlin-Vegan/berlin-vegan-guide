package org.berlin_vegan.bvapp.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.LocationDetailActivity;
import org.berlin_vegan.bvapp.data.Location;
import org.berlin_vegan.bvapp.data.Locations;


public class LocationActionsFragment extends LocationBaseFragment {
    private Location mLocation;
    private TextView favoriteTextView;

    public LocationActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_actions_fragment, container, false);
        mLocation = initLocation(savedInstanceState);
        initDialButton(view);
        initFavoriteButton(view);
        initWebsiteButton(view);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(LocationDetailActivity.EXTRA_LOCATION, mLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initDialButton(View view) {
        final TextView dialTextView = (TextView) view.findViewById(R.id.text_view_dial);
        final boolean hasTelephone = !mLocation.getTelephone().isEmpty();
        setColorForTextView(dialTextView, hasTelephone);
        if (hasTelephone) {
            dialTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialPhoneButtonClicked();
                }
            });
        }
    }

    private void initWebsiteButton(View view) {
        // website
        final TextView websiteTextView = (TextView) view.findViewById(R.id.text_view_website);
        final boolean hasWebsite = !mLocation.getWebsite().isEmpty();
        setColorForTextView(websiteTextView, hasWebsite);
        if (hasWebsite) {
            websiteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    websiteButtonClicked();
                }
            });
        }
    }

    private void initFavoriteButton(View view) {
        favoriteTextView = (TextView) view.findViewById(R.id.text_view_favorite);
        setFavoriteIcon(isFavorite());
        favoriteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteButtonClicked();
            }
        });
    }

    private void websiteButtonClicked() {
        Uri url = Uri.parse(mLocation.getWebsiteWithProtocolPrefix());
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void favoriteButtonClicked() {
        if (!isFavorite()) {
            setFavoriteIcon(true);
            Locations.addFavorite(getActivity(), mLocation.getId());
        } else {
            setFavoriteIcon(false);
            Locations.removeFavorite(getActivity(), mLocation.getId());
        }
    }

    private void dialPhoneButtonClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mLocation.getTelephone()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void setFavoriteIcon(boolean isFavorite) {
        final Drawable favoriteIcon;
        if (isFavorite) {
            favoriteIcon = getResources().getDrawable(R.mipmap.ic_star_white_24dp);
        } else {
            favoriteIcon = getResources().getDrawable(R.mipmap.ic_star_outline_white_24dp);
        }
        favoriteTextView.setCompoundDrawablesWithIntrinsicBounds(null, favoriteIcon, null, null);
        setColorForTextView(favoriteTextView, true);
    }

    private boolean isFavorite() {
        return Locations.containsFavorite(mLocation.getId());
    }

    private void setColorForTextView(TextView textView, boolean enabled) {
        final int enabledColor = getResources().getColor(R.color.theme_primary);
        final int disabledColor = getResources().getColor(R.color.disabled);
        if (enabled) {
            textView.setTextColor(enabledColor);
        } else {
            textView.setTextColor(disabledColor);
        }
        final Drawable drawable = textView.getCompoundDrawables()[1];
        if (drawable != null) { // if top drawable is != null
            if (enabled) {
                drawable.setColorFilter(enabledColor, PorterDuff.Mode.MULTIPLY);
            } else {
                drawable.setColorFilter(disabledColor, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

}
