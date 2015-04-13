package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class GastroDescriptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastro_description_activity);
        String title;
        String description;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                title = "";
                description = "";
            } else {
                title = extras.getString("TITLE");
                description = extras.getString("DESCRIPTION");
            }
        } else {
            title = (String) savedInstanceState.getSerializable("TITLE");
            description = (String) savedInstanceState.getSerializable("DESCRIPTION");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        TextView vDescription = (TextView) findViewById(R.id.text_view_description);
        // the description is html content and fromHtml() returns type Spanned
        vDescription.setText(Html.fromHtml(description), TextView.BufferType.SPANNABLE);
        vDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}
