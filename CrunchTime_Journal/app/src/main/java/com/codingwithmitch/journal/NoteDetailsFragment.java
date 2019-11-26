package com.codingwithmitch.journal;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingwithmitch.journal.models.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends Fragment {


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

        if(getActivity().getIntent().hasExtra("selected_note")) {
            Note note = getActivity().getIntent().getParcelableExtra("selected_note");
            paper.setText(note.getContent());
        }

        return view;
    }

}
