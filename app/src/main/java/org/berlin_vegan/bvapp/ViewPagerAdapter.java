package org.berlin_vegan.bvapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence mTitles[];
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mTitles = new CharSequence[]{context.getString(R.string.details), context.getString(R.string.description)};
        mNumOfTabs = mTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            GastroDetailsFragment gastroDetailsFragment = new GastroDetailsFragment();
            return gastroDetailsFragment;
        } else {
            GastroDescriptionFragment gastroDescriptionFragment = new GastroDescriptionFragment();
            return gastroDescriptionFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
