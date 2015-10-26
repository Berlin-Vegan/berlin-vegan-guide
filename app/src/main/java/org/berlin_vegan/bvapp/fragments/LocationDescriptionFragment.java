package org.berlin_vegan.bvapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.LocationDetailActivity;
import org.berlin_vegan.bvapp.data.Location;
import org.berlin_vegan.bvapp.views.ExpandableTextView;

/**
 * Holds content for the description tab in {@link LocationDetailActivity}.
 */
public class LocationDescriptionFragment extends LocationBaseFragment {

    private Location mLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_description_fragment, container, false);
        mLocation = initLocation(savedInstanceState);

        ExpandableTextView vDescription = (ExpandableTextView) v.findViewById(R.id.text_view_description);
        String description = mLocation.getCommentWithoutSoftHyphens();
        // remove all occurrences of '<br/>' at the end of the description so we have no space between
        // the description and the {@link DividerFragment}
        final String lineBreak = "<br/>";
        final int lineBreakLength = lineBreak.length();
        int descriptionLength = description.length();
        int lineBreakIndex = description.lastIndexOf(lineBreak);
        while (descriptionLength == lineBreakIndex + lineBreakLength) {
            description = description.substring(0, lineBreakIndex);
            descriptionLength = description.length();
            lineBreakIndex = description.lastIndexOf(lineBreak);
        }
        // the description is html content and fromHtml() returns type Spanned
        vDescription.setText(Html.fromHtml(description), TextView.BufferType.SPANNABLE);
        vDescription.setMovementMethod(new ScrollingMovementMethod());
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(LocationDetailActivity.EXTRA_LOCATION, mLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
