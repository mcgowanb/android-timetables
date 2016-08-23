package com.mcgowan.timetable.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mcgowan.timetable.android.data.TimetableContract;

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
                Fragment fragment = new NextClassFragment();
                Bundle bundle = new Bundle();
                String uri  = TimetableContract.TimetableEntry
                        .buildTimetableUri(107).toString();
                bundle.putString("URI", uri);
                fragment.setArguments(bundle);
                return fragment;
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
