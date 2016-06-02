package com.mcgowan.timetable.itsligotimetables;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mcgowan.timetable.scraper.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableFragment extends Fragment {

    public TimeTableFragment() {
    }

    ArrayAdapter<String> mTimetableAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timetablefragmemt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            FetchTimeTableTask task = new FetchTimeTableTask();
            task.execute();
            Toast.makeText(getActivity(), "Refreshing",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
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

        ListView lv = (ListView) rootView.findViewById(R.id.listview_timetable);
        lv.setAdapter(mTimetableAdapter);


        return rootView;
    }
}