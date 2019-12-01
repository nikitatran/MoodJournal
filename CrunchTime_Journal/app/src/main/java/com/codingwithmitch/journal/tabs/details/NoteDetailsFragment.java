package com.codingwithmitch.journal.tabs.details;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingwithmitch.journal.DetailsActivity;
import com.codingwithmitch.journal.LinedEditText;
import com.codingwithmitch.journal.R;
import com.codingwithmitch.journal.models.Note;

import java.util.ArrayList;

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
        LinedEditText paper = getView().findViewById(R.id.note_text);
        String s = ((DetailsActivity)getActivity()).getEditedContent();
        if(s.length() > 0){
            paper.setText(s);
        }
    }
}
