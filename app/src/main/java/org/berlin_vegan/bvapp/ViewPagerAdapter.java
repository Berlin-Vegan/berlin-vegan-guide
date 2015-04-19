package org.berlin_vegan.bvapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int numOfTabs;

    public ViewPagerAdapter(FragmentManager fragmentManager, CharSequence titles[], int numOfTabs) {
        super(fragmentManager);
        this.titles = titles;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            GastroDescriptionFragment gastroDescriptionFragment = new GastroDescriptionFragment();
            return gastroDescriptionFragment;
        } else {
            GastroDetailsFragment gastroDetailsFragment = new GastroDetailsFragment();
            return gastroDetailsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
