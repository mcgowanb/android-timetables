package com.mcgowan.timetable.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagesAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabPagesAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new TimeTableTodayFragment();
            case 1:
                return new TimeTableWeekFragment();
//            case 2:
//                return Tab3.newInstance(position + 1);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_today);
            case 1:
                return mContext.getString(R.string.title_week);
//            case 2:
//                return getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }


}
