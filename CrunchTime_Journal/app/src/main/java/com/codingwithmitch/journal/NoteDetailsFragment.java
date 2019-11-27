package com.codingwithmitch.journal;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends Fragment {
    private static final String TAG = "detailsfragment";
    private ArrayList<Note> mNotes = new ArrayList<>();

    public NoteDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_note, container, false);
        View toolbar = view.findViewById(R.id.note_toolbar);
        toolbar.setVisibility(View.GONE);

        LinedEditText paper = view.findViewById(R.id.note_text);
        paper.setKeyListener(null);
        //initialize
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        paper.setText(note.getContent());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG, "onresume called in fragment");
        LinedEditText paper = getView().findViewById(R.id.note_text);
        String s = ((DetailsActivity)getActivity()).getEditedContent();
        Log.d(TAG, "edited_content: " + s);
        if(s.length() > 0){
            paper.setText(s);
        }
    }
}
