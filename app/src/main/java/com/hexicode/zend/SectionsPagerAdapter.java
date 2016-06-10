package com.hexicode.zend;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    MyZendCardsFragment myZendCardsFragment;
    Activity mActivity;
    CardsFragment cardsFragment;

    public SectionsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        myZendCardsFragment = new MyZendCardsFragment();
        cardsFragment = new CardsFragment();
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return myZendCardsFragment;
            case 1:
                return cardsFragment;
            case 2:
                return PlaceholderFragment.newInstance(position);
            default:
                return PlaceholderFragment.newInstance(position);
        }

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mActivity.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mActivity.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return mActivity.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
}