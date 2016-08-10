package com.mcgowan.timetable.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a Fragment (defined as a static inner class below).
        switch (position) {
            case 0:
            case 1:
                return TimeTableFragment.newInstance(position);
            case 2:
                //todo add the single day here
                return TimeTableFragment.newInstance(position);
        }
        return null;
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
                return mContext.getString(R.string.today_title);
            case 1:
                return mContext.getString(R.string.week_title);
            case 2:
                return mContext.getString(R.string.next_title);
        }
        return null;
    }

}
