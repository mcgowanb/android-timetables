package com.mcgowan.timetable.itsligotimetables;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    ArrayAdapter<String> mTimetableAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> sampleTimetable = new ArrayList<>();
        sampleTimetable.add("Class One");
        sampleTimetable.add("Class Two");
        sampleTimetable.add("Class Three");
        sampleTimetable.add("Class Four");
        sampleTimetable.add("Class Five");

        mTimetableAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_class,
                R.id.list_item_class_textview,
                sampleTimetable);

        ListView lv = (ListView)rootView.findViewById(R.id.listview_timetable);
        lv.setAdapter(mTimetableAdapter);


        return rootView;
    }
}
