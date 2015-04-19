package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GastroDescriptionFragment extends Fragment {

    private String mDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gastro_description_fragment, container, false);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                mDescription = "";
            } else {
                mDescription = extras.getString("DESCRIPTION");
            }
        } else {
            mDescription = (String) savedInstanceState.getSerializable("DESCRIPTION");
        }
        TextView vDescription = (TextView) v.findViewById(R.id.text_view_description);
        // the description is html content and fromHtml() returns type Spanned
        vDescription.setText(Html.fromHtml(mDescription), TextView.BufferType.SPANNABLE);
        vDescription.setMovementMethod(new ScrollingMovementMethod());
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("DESCRIPTION", mDescription);
        super.onSaveInstanceState(savedInstanceState);
    }
}
