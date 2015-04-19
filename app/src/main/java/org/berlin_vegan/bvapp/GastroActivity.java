package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

public class GastroActivity extends BaseActivity {

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastro_activity);
        // set title
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mTitle = "";
            } else {
                mTitle = extras.getString("TITLE");
            }
        } else {
            mTitle = (String) savedInstanceState.getSerializable("TITLE");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mTitle);
        // tab handling
        CharSequence[] titles = {getString(R.string.description), getString(R.string.details)};
        int numOfTabs = titles.length;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.theme_primary_dark);
            }
        });
        tabs.setViewPager(pager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("TITLE", mTitle);
        super.onSaveInstanceState(savedInstanceState);
    }
}
