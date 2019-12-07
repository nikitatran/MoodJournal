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

package cpsc4150.projects.journal.tabs.main;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;

import com.projects.journal.R;
import cpsc4150.projects.journal.database.NoteRepository;
import cpsc4150.projects.journal.models.Note;
import cpsc4150.projects.journal.util.Utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays the Breakdown Screen tab for the Breakdown page located on the main screen
 */
public class BreakdownFragment extends Fragment {

    private static final String TAG = "breakdown";
    private ArrayList<Note> mNotes7 = new ArrayList<>();
    private ArrayList<Note> mNotes30 = new ArrayList<>();
    private NoteRepository mNoteRepository;

    public BreakdownFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_breakdown, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Variables that represent milliseconds in a week, and in 30 days
        long timestamp7 = Long.valueOf(Utility.getCurrentEpochMilli()) - (DateUtils.WEEK_IN_MILLIS);
        long timestamp30 = Long.valueOf(Utility.getCurrentEpochMilli()) - (DateUtils.DAY_IN_MILLIS * 7);

        mNoteRepository = new NoteRepository(getActivity());

        /**
         * An observer that listens for any updates in the database.
         * When the database is updated, the emotional analysis that calculates emotions in a
         * seven day period is recalculated. Then, setView is called to update the view.
         */

        mNoteRepository.retrieveNotesByTimeTask(timestamp7).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                double sad = 0.0;
                double happy = 0.0;
                double excited = 0.0;
                double fear = 0.0;
                double angry = 0.0;
                double bored = 0.0;
                if (notes.size() > 0) {
                    if (mNotes7.size() > 0) {
                        mNotes7.clear();
                    }

                    if (notes != null) {
                        mNotes7.addAll(notes);
                    }
                    int list_size = mNotes7.size();

                    for (int i = 0; i < list_size; i++) {
                        sad = sad + mNotes7.get(i).getSad();
                        happy = happy + mNotes7.get(i).getHappy();
                        angry = angry + mNotes7.get(i).getAngry();
                        excited = excited + mNotes7.get(i).getExcited();
                        fear = fear + mNotes7.get(i).getFear();
                        bored = bored + mNotes7.get(i).getBored();
                    }
                    setViews(getView(), sad, happy, angry, excited, fear, bored);
                }
            }
        });
        /**
         * An observer that listens for any updates in the database.
         * When the database is updated, the emotional analysis that calculates emotions in a
         * thirty day period is recalculated. Then, setView30 is called to update the view.
         *
         * Observer based off of reference 1
         */
        mNoteRepository.retrieveNotesByTimeTask(timestamp30).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                double sad = 0;
                double happy = 0;
                double excited = 0;
                double fear = 0;
                double angry = 0;
                double bored = 0;
                if (notes.size() > 0) {
                    if (mNotes30.size() > 0) {
                        mNotes30.clear();
                    }

                    if (notes != null) {
                        mNotes30.addAll(notes);
                    }
                    int list_size = mNotes30.size();

                    for (int i = 0; i < list_size; i++) {
                        sad = sad + mNotes30.get(i).getSad();
                        happy = happy + mNotes30.get(i).getHappy();
                        angry = angry + mNotes30.get(i).getAngry();
                        excited = excited + mNotes30.get(i).getExcited();
                        fear = fear + mNotes30.get(i).getFear();
                        bored = bored + mNotes30.get(i).getBored();
                    }
                    setViews30(getView(), sad, happy, angry, excited, fear, bored);
                }
            }
        });
    }

    /**
     * Function setViews display the view reflecting the past seven days worth of emotions for the user to see.
     * Pre-condition: Requires the following variables of:
     * @param view, the current view of the activity
     * @param sad a double value that represents the percentage of the sadness emotion calculated from a certain note input
     * @param happy a double value that represents the percentage of the happiness emotion calculated from a certain note input
     * @param angry a double value that represents the percentage of the anger emotion calculated from a certain note input
     * @param excited a double value that represents the percentage of the excited emotion calculated from a certain note input
     * @param fear a double value that represents the percentage of the fear emotion calculated from a certain note input
     * @param bored a double value that represents the percentage of the bored emotion calculated from a certain note input
     * Post-Condition: Sets the Breakdown Fragment view to reflect the emotion values that are passed in through the functions that the
     * user will see
     *
     *  DecimalFormat and RoundingMode referenced from 2 & 3
     */
    private void setViews(View view, double sad, double happy, double angry, double excited, double fear, double bored) {
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValueBreakdown);
        TextView sadValue = view.findViewById(R.id.sadValueBreakdown);
        TextView angryValue = view.findViewById(R.id.angryValueBreakdown);
        TextView excitedValue = view.findViewById(R.id.excitedValueBreakdown);
        TextView boredValue = view.findViewById(R.id.boredValueBreakdown);
        TextView fearValue = view.findViewById(R.id.fearValueBreakdown);

        double emotionSum = sad + happy + angry + excited + fear + bored;
        double happyAvg = (happy/emotionSum) * 100;
        double sadAvg = (sad/emotionSum) * 100;
        double boredAvg = (bored/emotionSum) * 100;
        double angryAvg = (angry/emotionSum) * 100;
        double excitedAvg = (excited/emotionSum) * 100;
        double fearAvg = (fear/emotionSum) * 100;

        happyValue.setText(df.format(happyAvg) + "%");
        sadValue.setText(df.format(sadAvg) + "%");
        boredValue.setText(df.format(boredAvg) + "%");
        angryValue.setText(df.format(angryAvg) + "%");
        excitedValue.setText(df.format(excitedAvg) + "%");
        fearValue.setText(df.format(fearAvg) + "%");
    }

    /**
     * Function setViews display the view reflecting the past thirty days worth of emotions for the user to see.
     * Pre-condition: Requires the following variables of:
     * @param view, the current view of the activity
     * @param sad a double value that represents the percentage of the sadness emotion calculated from a certain note input
     * @param happy a double value that represents the percentage of the happiness emotion calculated from a certain note input
     * @param angry a double value that represents the percentage of the anger emotion calculated from a certain note input
     * @param excited a double value that represents the percentage of the excited emotion calculated from a certain note input
     * @param fear a double value that represents the percentage of the fear emotion calculated from a certain note input
     * @param bored a double value that represents the percentage of the bored emotion calculated from a certain note input
     * Post-Condition: Sets the Breakdown Fragment view to reflect the emotion values that are passed in through the functions that the
     * user will see
     *
     * DecimalFormat and RoundingMode referenced from 2 & 3
     */
    private void setViews30(View view, double sad, double happy, double angry, double excited, double fear, double bored) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValueBreakdown30);
        TextView sadValue = view.findViewById(R.id.sadValueBreakdown30);
        TextView angryValue = view.findViewById(R.id.angryValueBreakdown30);
        TextView excitedValue = view.findViewById(R.id.excitedValueBreakdown30);
        TextView boredValue = view.findViewById(R.id.boredValueBreakdown30);
        TextView fearValue = view.findViewById(R.id.fearValueBreakdown30);

        double emotionSum = sad + happy + angry + excited + fear + bored;

        double happyAvg = (happy/emotionSum) * 100;
        double sadAvg = (sad/emotionSum) * 100;
        double boredAvg = (bored/emotionSum) * 100;
        double angryAvg = (angry/emotionSum) * 100;
        double excitedAvg = (excited/emotionSum) * 100;
        double fearAvg = (fear/emotionSum) * 100;

        happyValue.setText(df.format(happyAvg) + "%");
        sadValue.setText(df.format(sadAvg) + "%");
        boredValue.setText(df.format(boredAvg) + "%");
        angryValue.setText(df.format(angryAvg) + "%");
        excitedValue.setText(df.format(excitedAvg) + "%");
        fearValue.setText(df.format(fearAvg) + "%");
    }
}