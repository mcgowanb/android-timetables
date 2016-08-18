package com.mcgowan.timetable.android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgowan.timetable.android.R;

import us.feras.mdv.MarkdownView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutActivityFragment extends Fragment {

    private static final String MARKDOWN_URL = "file:///android_asset/about.md";

    public AboutActivityFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addFloatingActionBar();
        MarkdownView markdownView = (MarkdownView) getActivity().findViewById(R.id.about_markdownView);
        markdownView.loadMarkdownFile(MARKDOWN_URL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private void addFloatingActionBar() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "mcgowan.b@gmail.com";
                String subject = "IT Sligo Timetables App Query";
                String chooserTitle = "Choose an app....";
                Uri uri = Uri.parse("mailto:" + email)
                        .buildUpon()
                        .appendQueryParameter("subject", subject)
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, chooserTitle));

            }
        });
    }
}
