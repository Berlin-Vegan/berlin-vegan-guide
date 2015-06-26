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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        addOpeningHours(v);
        addTelephone(v);
        addWebsite(v);

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

    private void addOpeningHours(final View v) {
        final RelativeLayout item = (RelativeLayout) v.findViewById(R.id.opening_hours);

        final ImageView icon = (ImageView) item.findViewById(R.id.icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_schedule_white_24dp));
        icon.setColorFilter(getResources().getColor(R.color.theme_primary));

        final LinearLayout content = (LinearLayout) item.findViewById(R.id.content);

        final List<List<String>> dates = new ArrayList<>();
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_monday), mGastroLocation.getOtMon()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_tuesday), mGastroLocation.getOtTue()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_wednesday), mGastroLocation.getOtWed()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_thursday), mGastroLocation.getOtThu()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_friday), mGastroLocation.getOtFri()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_saturday), mGastroLocation.getOtSat()));
        dates.add(Arrays.asList(getString(R.string.gastro_details_opening_hours_content_sunday), mGastroLocation.getOtSun()));

        for (List<String> date : dates) {
            final LinearLayout dateLayout = new LinearLayout(v.getContext());
            dateLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            dateLayout.setPadding(0, 0, 0, 16);
            dateLayout.setOrientation(LinearLayout.HORIZONTAL);

            final TextView key = new TextView(v.getContext());
            key.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.7f));
            key.setText(date.get(0));

            dateLayout.addView(key);

            final TextView value = new TextView(getActivity());
            value.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.3f));
            final String s = date.get(1);
            if (!s.trim().equals("")) {
                value.setText(s);
            } else {
                value.setText(getString(R.string.gastro_details_opening_hours_content_closed));
            }

            dateLayout.addView(value);

            content.addView(dateLayout);
        }
    }

    private void addTelephone(final View v) {
        final RelativeLayout item = (RelativeLayout) v.findViewById(R.id.telephone);

        final ImageView icon = (ImageView) item.findViewById(R.id.icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_phone_white_24dp));
        icon.setColorFilter(getResources().getColor(R.color.theme_primary));

        final String text;
        final String telephone = mGastroLocation.getTelephone();
        if (telephone != null) {
            text = "<a href=\"tel:"
                    + telephone + "\">"
                    + telephone + "</a>";
        } else {
            text = getString(R.string.gastro_details_contact_telephone);
        }
        final TextView content = (TextView) item.findViewById(R.id.content);
        content.setText(Html.fromHtml(text));
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addWebsite(final View v) {
        final RelativeLayout item = (RelativeLayout) v.findViewById(R.id.website);

        final ImageView icon = (ImageView) item.findViewById(R.id.icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_public_white_24dp));
        icon.setColorFilter(getResources().getColor(R.color.theme_primary));

        final String text;
        final String website = mGastroLocation.getWebsite();
        if (website != null) {
            text = "<a href="
                    + website + ">"
                    + website + "</a>";
        } else {
            text = getString(R.string.gastro_details_contact_website);
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
