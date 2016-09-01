package com.mcgowan.timetable.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagesAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private static final String LOG_TAG = FragmentPagerAdapter.class.getSimpleName();

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
                return new NextClassFragment();
            case 1:
                return new TimeTableTodayFragment();
            case 2:
                return new TimeTableWeekFragment();
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
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_next);
            case 1:
                return mContext.getString(R.string.title_today);
            case 2:
                return mContext.getString(R.string.title_week);
        }
        return null;
    }


}
