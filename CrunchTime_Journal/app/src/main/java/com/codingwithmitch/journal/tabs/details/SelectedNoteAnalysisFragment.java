package com.codingwithmitch.journal.tabs.details;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingwithmitch.journal.DetailsActivity;
import com.codingwithmitch.journal.R;
import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.models.Note;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedNoteAnalysisFragment extends Fragment {
    public static final String TAG = "AnalysisFrag";

    public SelectedNoteAnalysisFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_note_analysis, container, false);
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        setViews(view, note);

        Log.d("TAG", "oncreateview called");
        return view;
    }

    private ArrayList<Note> mNotes = new ArrayList<>();
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onresume called");
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        NoteRepository mNoteRepository = new NoteRepository(getActivity());
        mNoteRepository.getNoteById(note.getId()).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(notes.size() > 0) {
                    if (mNotes.size() > 0) {
                        mNotes.clear();
                    }
                    if (notes != null) {
                        mNotes.addAll(notes);
                    }
                    Note note = mNotes.get(0);

                    setViews(getView(), note);
                }
            }
        });

    }

    private void setViews(View view, Note note){
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        TextView happyValue = view.findViewById(R.id.happyValue);
        TextView sadValue = view.findViewById(R.id.sadValue);
        TextView angryValue = view.findViewById(R.id.angryValue);
        TextView excitedValue = view.findViewById(R.id.excitedValue);
        TextView boredValue = view.findViewById(R.id.boredValue);
        TextView fearValue = view.findViewById(R.id.fearValue);

        happyValue.setText(df.format(note.getHappy()));
        sadValue.setText(df.format(note.getSad()));
        boredValue.setText(df.format(note.getBored()));
        angryValue.setText(df.format(note.getAngry()));
        excitedValue.setText(df.format(note.getExcited()));
        fearValue.setText(df.format(note.getFear()));
    }
}
