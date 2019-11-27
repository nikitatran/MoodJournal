package com.codingwithmitch.journal.tabs.main;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * BreakdownFragment returns the fragment that displays the breakdown tab
 */
public class BreakdownFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "breakdown";
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRepository mNoteRepository;


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
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_breakdown, container, false);

        //TODO Return the array of notes from the last 7 days
        Long timestamp = Long.valueOf (Utility.getCurrentEpochMilli()) - 604800000;

        NoteDatabase appDatabase = NoteDatabase.getInstance (getActivity ());
        appDatabase.getNoteDao ().getNotes7Days (timestamp);

        mNoteRepository = new NoteRepository(getActivity());
        //                |
        //                V added this function in NoteRepository
        mNoteRepository.retrieveNotes7daysTask(timestamp).observe(this, new Observer<List<Note>>() {
            /*
                every time a new note is added, onChanged will run
                so this takes away the trouble of doing callbacks every time the database changes

                downside is that this is asynchronous so it may be better to just code everything
                inside onChanged()
                for example list_size is 0 outside of this function even when there are notes available
                but inside it shows the correct number of notes
             */
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }

                int list_size = mNotes.size();
                Log.d(TAG, "list size: "+list_size);

                Log.d(TAG, "content: " + mNotes.get(0).getContent());
                double happy = mNotes.get(0).getHappy(); //position 0 would be the oldest entry in the list that got returned
                Log.d(TAG, "happy: "+happy);

                /*
                    step-by-step of my testing:
                    - i made 4 dummy notes
                    - timestamps should be recent so the query won't filter them out
                    - size returned correctly (returned 4)
                    - opened the database file
                    - edited the time for 1 file (changed milliseconds to be in 2009)
                    - uploaded it to the database folder in device file manager
                    - ran the app again
                    - log says list size is 3 (so 1 entry got filtered out by the query)

                    so from what ive done, looks like it's working.
                    search "breakdown" in logcat to see for yourself
                 */
            }
        });

        return root;
    }
}