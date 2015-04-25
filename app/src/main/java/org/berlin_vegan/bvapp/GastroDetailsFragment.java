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
        return v;
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
