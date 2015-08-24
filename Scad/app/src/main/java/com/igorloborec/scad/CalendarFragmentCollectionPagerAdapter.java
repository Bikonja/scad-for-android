package com.igorloborec.scad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Bikonja on 24.8.2015..
 */
public class CalendarFragmentCollectionPagerAdapter extends FragmentStatePagerAdapter {

    static final int NUM_ITEMS = 365 * 2;
    static final int FIRST_ITEM = NUM_ITEMS / 2;

    private CalendarFragment[] mCalendarFragments = new CalendarFragment[NUM_ITEMS];

    public CalendarFragmentCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        CalendarFragment fragment = mCalendarFragments[i];

        if (fragment == null) {
            fragment = new CalendarFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("ADD", i - FIRST_ITEM);
            fragment.setArguments(bundle);
            mCalendarFragments[i] = fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mCalendarFragments[position] = null;
    }
}
