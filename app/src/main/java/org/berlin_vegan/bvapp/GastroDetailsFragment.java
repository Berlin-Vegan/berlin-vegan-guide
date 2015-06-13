package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class GastroDetailsFragment extends Fragment {

    private GastroLocation mGastroLocation;

    final public static int OMNIVORE = 1;
    final public static int OMNIVORE_VEGAN_DECLARED = 2;
    final public static int VEGETARIAN = 3;
    final public static int VEGETARIAN_VEGAN_DECLARED = 4;
    final public static int VEGAN = 5;

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
        fillGastroDetailsContactContent(v);
        fillGastroDetailsOpeningHoursContent(v);
        fillGastroDetailsMiscellaneousContent(v);
        return v;
    }

    private void fillGastroDetailsMiscellaneousContent(View v) {
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
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(0, 0, 0, 4);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            // key
            TextView vMiscellaneousKey = new TextView(getActivity());
            vMiscellaneousKey.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.6f));
            vMiscellaneousKey.setText(miscellaneousData[i][0]);
            vMiscellaneousKey.setTextColor(getResources().getColor(R.color.theme_primary));
            linearLayout.addView(vMiscellaneousKey);
            // value
            TextView vMiscellaneousValue = new TextView(getActivity());
            vMiscellaneousKey.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.4f));
            vMiscellaneousValue.setGravity(Gravity.END);
            vMiscellaneousValue.setText(miscellaneousData[i][1]);
            vMiscellaneousValue.setTextColor(getResources().getColor(R.color.theme_primary_secondary_text));
            linearLayout.addView(vMiscellaneousValue);
            gastroDetailsMiscellaneousContent.addView(linearLayout);
        }
    }

    private void fillGastroDetailsOpeningHoursContent(View v) {
        LinearLayout gastroDetailsOpeningHoursContent = (LinearLayout) v.findViewById(R.id.gastro_details_opening_hours_content);
        String[][] openingHoursData = {
                {getString(R.string.gastro_details_opening_hours_content_monday), mGastroLocation.getOtMon()},
                {getString(R.string.gastro_details_opening_hours_content_tuesday), mGastroLocation.getOtTue()},
                {getString(R.string.gastro_details_opening_hours_content_wednesday), mGastroLocation.getOtWed()},
                {getString(R.string.gastro_details_opening_hours_content_thursday), mGastroLocation.getOtThu()},
                {getString(R.string.gastro_details_opening_hours_content_friday), mGastroLocation.getOtFri()},
                {getString(R.string.gastro_details_opening_hours_content_saturday), mGastroLocation.getOtSat()},
                {getString(R.string.gastro_details_opening_hours_content_sunday), mGastroLocation.getOtSun()}
        };
        for (int i = 0; i < openingHoursData.length; i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(0, 0, 0, 4);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            // view day of the week
            TextView vDayOfTheWeek = new TextView(getActivity());
            vDayOfTheWeek.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.6f));
            vDayOfTheWeek.setText(openingHoursData[i][0]);
            vDayOfTheWeek.setTextColor(getResources().getColor(R.color.theme_primary));
            linearLayout.addView(vDayOfTheWeek);
            // view opening hours
            TextView vOpeningHours = new TextView(getActivity());
            vOpeningHours.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.4f));
            vOpeningHours.setGravity(Gravity.END);
            if (!openingHoursData[i][1].trim().equals("")) {
                vOpeningHours.setText(openingHoursData[i][1]);
            } else {
                vOpeningHours.setText(getString(R.string.gastro_details_opening_hours_content_closed));
            }
            vOpeningHours.setTextColor(getResources().getColor(R.color.theme_primary_secondary_text));
            linearLayout.addView(vOpeningHours);
            gastroDetailsOpeningHoursContent.addView(linearLayout);
        }
    }

    private void fillGastroDetailsContactContent(View v) {
        if (mGastroLocation.getStreet() != null) {
            fillGastroDetailsAddressView(v);
        }
        if (mGastroLocation.getTelephone() != null) {
            fillGastroDetailsTelephoneView(v);
        }
    }

    private void fillGastroDetailsTelephoneView(View v) {
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

    private void fillGastroDetailsAddressView(View v) {
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

    private String getVeganContentString(int i) {
        if (i == OMNIVORE) {
            return getString(R.string.gastro_details_miscellaneous_content_omnivore);
        } else if (i == OMNIVORE_VEGAN_DECLARED) {
            return getString(R.string.gastro_details_miscellaneous_content_omnivore_vegan_declared);
        } else if (i == VEGETARIAN) {
            return getString(R.string.gastro_details_miscellaneous_content_vegetarian);
        } else if (i == VEGETARIAN_VEGAN_DECLARED) {
            return getString(R.string.gastro_details_miscellaneous_content_vegetarian_vegan_declared);
        } else if (i == VEGAN) {
            return getString(R.string.gastro_details_miscellaneous_content_completely_vegan);
        }
        return getString(R.string.gastro_details_miscellaneous_content_unknown);
    }

    private String getMiscellaneousContentString(int i) {
        if (i == 1) {
            return getString(R.string.gastro_details_miscellaneous_content_yes);
        } else if (i == 0) {
            return getString(R.string.gastro_details_miscellaneous_content_no);
        }
        return getString(R.string.gastro_details_miscellaneous_content_unknown);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("GASTRO_LOCATION", mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
