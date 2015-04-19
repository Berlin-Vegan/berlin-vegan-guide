package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class GastroDescriptionActivity extends BaseActivity {

    private String mTitle;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastro_description_activity);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mTitle = "";
                mDescription = "";
            } else {
                mTitle = extras.getString("TITLE");
                mDescription = extras.getString("DESCRIPTION");
            }
        } else {
            mTitle = (String) savedInstanceState.getSerializable("TITLE");
            mDescription = (String) savedInstanceState.getSerializable("DESCRIPTION");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mTitle);
        TextView vDescription = (TextView) findViewById(R.id.text_view_description);
        // the description is html content and fromHtml() returns type Spanned
        vDescription.setText(Html.fromHtml(mDescription), TextView.BufferType.SPANNABLE);
        vDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("TITLE", mTitle);
        savedInstanceState.putSerializable("DESCRIPTION", mDescription);
        super.onSaveInstanceState(savedInstanceState);
    }
}
