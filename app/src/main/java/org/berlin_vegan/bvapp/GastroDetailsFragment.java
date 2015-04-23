package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("GASTRO_LOCATION", mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
