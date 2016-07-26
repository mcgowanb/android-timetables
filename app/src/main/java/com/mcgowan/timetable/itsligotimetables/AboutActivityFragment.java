package com.mcgowan.timetable.itsligotimetables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.feras.mdv.MarkdownView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutActivityFragment extends Fragment {

    private static final String MARKDOWN_URL = "https://raw.githubusercontent.com/mcgowanb/android-timetables/master/about.md";

    public AboutActivityFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MarkdownView markdownView = (MarkdownView) getActivity().findViewById(R.id.about_markdownView);
//        markdownView.loadMarkdown("## Hello Markdown");
        markdownView.loadMarkdownFile(MARKDOWN_URL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
