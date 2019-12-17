/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package cpsc4150.projects.journal.tabs.details;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cpsc4150.projects.journal.DetailsActivity;
import cpsc4150.projects.journal.util.LinedEditText;
import com.projects.journal.R;
import cpsc4150.projects.journal.models.Note;

import java.util.ArrayList;

public class NoteDetailsFragment extends Fragment {
    private static final String TAG = "detailsfragment";
    private ArrayList<Note> mNotes = new ArrayList<>();

    public NoteDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * pre: layout for note activity and toolbar exists
     * post: view is initialized with the note content and title at the state it was when selected
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_note, container, false);
        View toolbar = view.findViewById(R.id.note_toolbar);
        toolbar.setVisibility(View.GONE);

        LinedEditText paper = view.findViewById(R.id.note_text);
        //Make EditText do nothing when clicked
        paper.setKeyListener(null);

        //Initialize view with the note at the state it was when selected
        Note note = getActivity().getIntent().getParcelableExtra("selected_note");
        paper.setText(note.getContent());

        return view;
    }

    /**
     *  pre: edit/create note screen activity destroyed
     *  post: Edited content is reflected in view
     */
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
