package com.codingwithmitch.journal.tabs.main;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.codingwithmitch.journal.R;
import com.codingwithmitch.journal.database.NoteDatabase;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.util.Utility;

import java.util.ArrayList;

/**
 * BreakdownFragment returns the fragment that displays the breakdown tab
 */
public class BreakdownFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<Note> mNotes = new ArrayList<>();


    public static BreakdownFragment newInstance(int index) {
        BreakdownFragment fragment = new BreakdownFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO Return the array of notes from the last 7 days
        Long timestamp = Long.valueOf (Utility.getCurrentEpochMilli()) - 604800000;

        NoteDatabase appDatabase = NoteDatabase.getInstance (getActivity ());
        appDatabase.getNoteDao ().getNotes7Days (timestamp);




    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_breakdown, container, false);
        return root;
    }
}