/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)

    References used:
        1. https://codingwithmitch.com/courses/sqlite-room-persistence-android/queries-using-livedata/
        2. https://developer.android.com/reference/java/math/RoundingMode
        3. https://developer.android.com/reference/java/text/DecimalFormat

        Twitter Emoji graphics licensing:
        Copyright 2019 Twitter, Inc and other contributors
        Code licensed under the MIT License: http://opensource.org/licenses/MIT
        Graphics licensed under CC-BY 4.0: https://creativecommons.org/licenses/by/4.0/

 */

package cpsc4150.projects.journal.tabs.details;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.journal.R;
import cpsc4150.projects.journal.database.NoteRepository;
import cpsc4150.projects.journal.models.Note;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SelectedNoteAnalysisFragment extends Fragment {
    public static final String TAG = "AnalysisFrag";

    public SelectedNoteAnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_note_analysis, container, false);
        //get note that was selected in list view
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        //initialize view with emotion values of note in intent extra
        setViews(view, note);
        return view;
    }

    /**
     *  Used to update the emotion values in view after note is edited.
     *  Uses observer to detect when database query returns something.
     *
     *  pre: user pressed back button from NoteEditActivity
     *  post: view displays updated emotion values
     */
    private ArrayList<Note> mNotes = new ArrayList<>();
    @Override
    public void onResume() {
        super.onResume();
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        NoteRepository mNoteRepository = new NoteRepository(getActivity());

        //Observer based on reference 1
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

    /**
     *  Helper function to set text in views.
     *
     *  pre: fragment layout exists, parcelable note extra in intent exists
     *  post: emotion values set in view, 2 decimal places rounded down format
     *
     * @param view fragment layout
     * @param note the note the user selected to view
     *
     * DecimalFormat and RoundingMode referenced from 2 & 3
     */
    private void setViews(View view, Note note){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValue);
        TextView sadValue = view.findViewById(R.id.sadValue);
        TextView angryValue = view.findViewById(R.id.angryValue);
        TextView excitedValue = view.findViewById(R.id.excitedValue);
        TextView boredValue = view.findViewById(R.id.boredValue);
        TextView fearValue = view.findViewById(R.id.fearValue);

        happyValue.setText(df.format(note.getHappy())+"%");
        sadValue.setText(df.format(note.getSad())+"%");
        boredValue.setText(df.format(note.getBored())+"%");
        angryValue.setText(df.format(note.getAngry())+"%");
        excitedValue.setText(df.format(note.getExcited())+"%");
        fearValue.setText(df.format(note.getFear())+"%");
    }
}
