package com.mcgowan.timetable.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a Fragment
        switch (position) {
            case 1:
            case 2:
                return TimeTableFragment.newInstance(position);
            case 0:
                //todo add the single day here
                return TimeTableFragment.newInstance(position);
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.next_title);
            case 1:
                return mContext.getString(R.string.today_title);
            case 2:
                return mContext.getString(R.string.week_title);
        }
        return null;
    }


}
