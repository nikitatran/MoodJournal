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

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    public void onResume() {
        super.onResume();
        //TODO Return the array of notes from the last 7 days
        Long timestamp = Long.valueOf (Utility.getCurrentEpochMilli()) - 604800000;
        Long timestamp30 = Long.valueOf (Utility.getCurrentEpochMilli ()) - (864000000 *3);

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
                double sad = 0;
                double happy = 0;
                double excited = 0;
                double fear = 0;
                double angry = 0;
                double bored = 0;
                if(notes.size() > 0) {
                    if (mNotes.size() > 0) {
                        mNotes.clear();

                    }

                    if (notes != null) {
                        mNotes.addAll(notes);


                    }
                    int list_size = mNotes.size();
                    Log.d(TAG, "list size: " + list_size);

                    //TODO Create a loop that sums up the emotions in each note

                    for (int i = 0; i< list_size; i++){
                        sad = sad + mNotes.get(i).getSad ();  //position 0 would be the oldest entry in the list that got returned
                        happy = happy + mNotes.get(i).getHappy ();
                        angry = angry + mNotes.get(i).getAngry ();
                        excited = excited + mNotes.get(i).getExcited ();
                        fear = fear + mNotes.get(i).getFear ();
                        bored = bored + mNotes.get(i).getBored ();
                        setViews(getView (),sad,happy,angry,excited,fear,bored);
                    }



                    Log.d(TAG, "content: " + mNotes.get(0).getContent());
                    Log.d(TAG, "sad: " + sad);

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
                else if(mNotes.size () ==0){

                }
            }
        });


        mNoteRepository.retrieveNotes30daysTask(timestamp30).observe(this, new Observer<List<Note>>() {
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
                double sad = 0;
                double happy = 0;
                double excited = 0;
                double fear = 0;
                double angry = 0;
                double bored = 0;
                if(notes.size() > 0) {
                    if (mNotes.size() > 0) {
                        mNotes.clear();

                    }

                    if (notes != null) {
                        mNotes.addAll(notes);


                    }
                    int list_size = mNotes.size();
                    Log.d(TAG, "list size: " + list_size);

                    //TODO Create a loop that sums up the emotions in each note

                    for (int i = 0; i< list_size; i++){
                        sad = sad + mNotes.get(i).getSad ();  //position 0 would be the oldest entry in the list that got returned
                        happy = happy + mNotes.get(i).getHappy ();
                        angry = angry + mNotes.get(i).getAngry ();
                        excited = excited + mNotes.get(i).getExcited ();
                        fear = fear + mNotes.get(i).getFear ();
                        bored = bored + mNotes.get(i).getBored ();
                        setViews30(getView (),sad,happy,angry,excited,fear,bored);
                    }



                    Log.d(TAG, "content: " + mNotes.get(0).getContent());
                    Log.d(TAG, "sad: " + sad);

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
                else if(mNotes.size () ==0){

                }
            }
        });

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


        return root;
    }

    private void setViews(View view,double sad,double happy,double angry,double excited,double fear,double bored){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValueBreakdown);
        TextView sadValue = view.findViewById(R.id.sadValueBreakdown);
        TextView angryValue = view.findViewById(R.id.angryValueBreakdown);
        TextView excitedValue = view.findViewById(R.id.excitedValueBreakdown);
        TextView boredValue = view.findViewById(R.id.boredValueBreakdown);
        TextView fearValue = view.findViewById(R.id.fearValueBreakdown);

        double emotionSum = sad+happy+ angry+excited+fear+bored;

        happyValue.setText(df.format(happy/emotionSum)+"%");
        sadValue.setText(df.format(sad/emotionSum)+"%");
        boredValue.setText(df.format(bored/emotionSum)+"%");
        angryValue.setText(df.format(angry/emotionSum)+"%");
        excitedValue.setText(df.format(excited/emotionSum)+"%");
        fearValue.setText(df.format(fear/emotionSum)+"%");
    }


    private void setViews30(View view,double sad,double happy,double angry,double excited,double fear,double bored){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValueBreakdown30);
        TextView sadValue = view.findViewById(R.id.sadValueBreakdown30);
        TextView angryValue = view.findViewById(R.id.angryValueBreakdown30);
        TextView excitedValue = view.findViewById(R.id.excitedValueBreakdown30);
        TextView boredValue = view.findViewById(R.id.boredValueBreakdown30);
        TextView fearValue = view.findViewById(R.id.fearValueBreakdown30);

        double emotionSum = sad+happy+ angry+excited+fear+bored;

        happyValue.setText(df.format(happy/emotionSum)+"%");
        sadValue.setText(df.format(sad/emotionSum)+"%");
        boredValue.setText(df.format(bored/emotionSum)+"%");
        angryValue.setText(df.format(angry/emotionSum)+"%");
        excitedValue.setText(df.format(excited/emotionSum)+"%");
        fearValue.setText(df.format(fear/emotionSum)+"%");
    }

    private void setViewsDefault(View view){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView happyValue = view.findViewById(R.id.happyValueBreakdown);
        TextView sadValue = view.findViewById(R.id.sadValueBreakdown);
        TextView angryValue = view.findViewById(R.id.angryValueBreakdown);
        TextView excitedValue = view.findViewById(R.id.excitedValueBreakdown);
        TextView boredValue = view.findViewById(R.id.boredValueBreakdown);
        TextView fearValue = view.findViewById(R.id.fearValueBreakdown);



        happyValue.setText(df.format(0.00)+"%");
        sadValue.setText(df.format(0.00)+"%");
        boredValue.setText(df.format(0.00)+"%");
        angryValue.setText(df.format(0.00)+"%");
        excitedValue.setText(df.format(0.00)+"%");
        fearValue.setText(df.format(0.00)+"%");
    }


}