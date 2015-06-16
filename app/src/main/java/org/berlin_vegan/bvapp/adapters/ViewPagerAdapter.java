package org.berlin_vegan.bvapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.activities.GastroActivity;
import org.berlin_vegan.bvapp.fragments.GastroDescriptionFragment;
import org.berlin_vegan.bvapp.fragments.GastroDetailsFragment;

/**
 * Makes horizontal tab sliding in {@link GastroActivity} possible.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final CharSequence mTitles[];
    private final int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mTitles = new CharSequence[]{context.getString(R.string.details), context.getString(R.string.description)};
        mNumOfTabs = mTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new GastroDetailsFragment();
        } else {
            return new GastroDescriptionFragment();
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
