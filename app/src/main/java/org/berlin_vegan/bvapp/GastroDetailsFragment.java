package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GastroDetailsFragment extends Fragment {

    private GastroLocation mGastroLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gastro_details_fragment, container, false);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                mGastroLocation = (GastroLocation) extras.getSerializable("GASTRO_LOCATION");
            }
        } else {
            mGastroLocation = (GastroLocation) savedInstanceState.getSerializable("GASTRO_LOCATION");
        }
        // contact
        if (mGastroLocation.getStreet() != null) {
            StringBuilder address = new StringBuilder()
                    .append("<a href=\"http://maps.google.com/maps?q=")
                            // google maps: street, citycode, city
                    .append(mGastroLocation.getStreet())
                    .append(", ")
                    .append(mGastroLocation.getCityCode())
                    .append(", ")
                    .append(mGastroLocation.getCity())
                    .append("\">")
                            // view: "street\n citycode city-district"
                    .append(mGastroLocation.getStreet())
                    .append("<br>")
                    .append(mGastroLocation.getCityCode())
                    .append(" ")
                    .append(mGastroLocation.getCity())
                    .append("-")
                    .append(mGastroLocation.getDistrict())
                    .append("</a>");
            TextView vGastroDetailsAddress = (TextView) v.findViewById(R.id.gastro_details_address);
            vGastroDetailsAddress.setText(Html.fromHtml(address.toString()));
            vGastroDetailsAddress.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (mGastroLocation.getTelephone() != null) {
            StringBuilder telephone = new StringBuilder()
                    .append("<a href=\"tel:")
                    .append(mGastroLocation.getTelephone())
                    .append("\">")
                    .append(mGastroLocation.getTelephone())
                    .append("</a>");
            TextView vGastroDetailsTelephone = (TextView) v.findViewById(R.id.gastro_details_telephone);
            vGastroDetailsTelephone.setText(Html.fromHtml(telephone.toString()));
            vGastroDetailsTelephone.setMovementMethod(LinkMovementMethod.getInstance());
        }
        // opening hours
        LinearLayout gastroDetailsOpeningHoursContent = (LinearLayout) v.findViewById(R.id.gastro_details_opening_hours_content);
        String[][] data = {
                {getString(R.string.gastro_details_opening_hours_content_monday), mGastroLocation.getOtMon()},
                {getString(R.string.gastro_details_opening_hours_content_tuesday), mGastroLocation.getOtTue()},
                {getString(R.string.gastro_details_opening_hours_content_wednesday), mGastroLocation.getOtWed()},
                {getString(R.string.gastro_details_opening_hours_content_thursday), mGastroLocation.getOtThu()},
                {getString(R.string.gastro_details_opening_hours_content_friday), mGastroLocation.getOtFri()},
                {getString(R.string.gastro_details_opening_hours_content_saturday), mGastroLocation.getOtSat()},
                {getString(R.string.gastro_details_opening_hours_content_sunday), mGastroLocation.getOtSun()}
        };
        for (int i = 0; i < data.length; i++) {
            // view day of the week
            TextView vDayOfTheWeek = new TextView(getActivity());
            vDayOfTheWeek.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getPx(36)));
            vDayOfTheWeek.setEllipsize(TextUtils.TruncateAt.END);
            vDayOfTheWeek.setGravity(Gravity.BOTTOM);
            vDayOfTheWeek.setMaxLines(1);
            vDayOfTheWeek.setText(data[i][0]);
            vDayOfTheWeek.setTextColor(getResources().getColor(R.color.theme_primary));
            gastroDetailsOpeningHoursContent.addView(vDayOfTheWeek);
            // view opening hours
            TextView vOpeningHours = new TextView(getActivity());
            vOpeningHours.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            vOpeningHours.setMaxLines(1);
            if (!data[i][1].trim().equals("")) {
                vOpeningHours.setText(data[i][1]);
            } else {
                vOpeningHours.setText(getString(R.string.gastro_details_opening_hours_content_closed));
            }
            vOpeningHours.setTextColor(getResources().getColor(R.color.theme_primary_secondary_text));
            gastroDetailsOpeningHoursContent.addView(vOpeningHours);
        }
        // miscellaneous
        LinearLayout gastroDetailsMiscellaneousContent = (LinearLayout) v.findViewById(R.id.gastro_details_miscellaneous_content);
        String[][] miscellaneousData = {
                {getString(R.string.gastro_details_miscellaneous_content_catering), getMiscellaneousContentString(mGastroLocation.getCatering())},
                {getString(R.string.gastro_details_miscellaneous_content_child_chair), getMiscellaneousContentString(mGastroLocation.getChildChair())},
                {getString(R.string.gastro_details_miscellaneous_content_delivery), getMiscellaneousContentString(mGastroLocation.getDelivery())},
                {getString(R.string.gastro_details_miscellaneous_content_dog), getMiscellaneousContentString(mGastroLocation.getDog())},
                {getString(R.string.gastro_details_miscellaneous_content_gluten_free), getMiscellaneousContentString(mGastroLocation.getGlutenFree())},
                {getString(R.string.gastro_details_miscellaneous_content_handicapped_accessible), getMiscellaneousContentString(mGastroLocation.getHandicappedAccessible())},
                {getString(R.string.gastro_details_miscellaneous_content_handicapped_accessible_wc), getMiscellaneousContentString(mGastroLocation.getHandicappedAccessibleWc())},
                {getString(R.string.gastro_details_miscellaneous_content_organic), getMiscellaneousContentString(mGastroLocation.getOrganic())},
                {getString(R.string.gastro_details_miscellaneous_content_seats_indoor), getMiscellaneousContentString(mGastroLocation.getSeatsIndoor())},
                {getString(R.string.gastro_details_miscellaneous_content_seats_outdoor), getMiscellaneousContentString(mGastroLocation.getSeatsOutdoor())},
                {getString(R.string.gastro_details_miscellaneous_content_vegan), getVeganContentString(mGastroLocation.getVegan())},
                {getString(R.string.gastro_details_miscellaneous_content_wlan), getMiscellaneousContentString(mGastroLocation.getWlan())}};
        for (int i = 0; i < miscellaneousData.length; i++) {
            // key
            TextView vMiscellaneousKey = new TextView(getActivity());
            vMiscellaneousKey.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getPx(36)));
            vMiscellaneousKey.setEllipsize(TextUtils.TruncateAt.END);
            vMiscellaneousKey.setGravity(Gravity.BOTTOM);
            vMiscellaneousKey.setMaxLines(1);
            vMiscellaneousKey.setText(miscellaneousData[i][0]);
            vMiscellaneousKey.setTextColor(getResources().getColor(R.color.theme_primary));
            gastroDetailsMiscellaneousContent.addView(vMiscellaneousKey);
            // value
            TextView vMiscellaneousValue = new TextView(getActivity());
            vMiscellaneousValue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            vMiscellaneousValue.setEllipsize(TextUtils.TruncateAt.END);
            vMiscellaneousValue.setGravity(Gravity.BOTTOM);
            vMiscellaneousValue.setMaxLines(1);
            vMiscellaneousValue.setText(miscellaneousData[i][1]);
            vMiscellaneousValue.setTextColor(getResources().getColor(R.color.theme_primary_secondary_text));
            gastroDetailsMiscellaneousContent.addView(vMiscellaneousValue);
        }
        return v;
    }

    private String getVeganContentString(int i) {
        if (i == 1) {
            return getString(R.string.gastro_details_miscellaneous_content_omnivore);
        } else if (i == 2) {
            return getString(R.string.gastro_details_miscellaneous_content_omnivore_vegan_declared);
        } else if (i == 3) {
            return getString(R.string.gastro_details_miscellaneous_content_vegetarian);
        } else if (i == 4) {
            return getString(R.string.gastro_details_miscellaneous_content_vegetarian_vegan_declared);
        } else if (i == 5) {
            return getString(R.string.gastro_details_miscellaneous_content_completely_vegan);
        }
        return "";
    }

    private String getMiscellaneousContentString(int i) {
        if (i == 1) {
            return getString(R.string.gastro_details_miscellaneous_content_yes);
        } else if (i == 0) {
            return getString(R.string.gastro_details_miscellaneous_content_no);
        } else if (i == -1) {
            return getString(R.string.gastro_details_miscellaneous_content_unknown);
        }
        return "";
    }

    private int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("GASTRO_LOCATION", mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
