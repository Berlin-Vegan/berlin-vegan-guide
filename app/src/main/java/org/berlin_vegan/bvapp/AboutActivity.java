package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        final String version = BuildConfig.VERSION_GIT_DESCRIPTION;
        final TextView textView = (TextView) findViewById(R.id.version);
        textView.setText(version);
    }
}
