package com.codingwithmitch.journal.tabs.main;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
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
        long timestamp = Long.valueOf(Utility.getCurrentEpochMilli()) - (DateUtils.WEEK_IN_MILLIS);
        long timestamp30 = Long.valueOf(Utility.getCurrentEpochMilli()) - (DateUtils.YEAR_IN_MILLIS);

        mNoteRepository = new NoteRepository(getActivity());
        mNoteRepository.retrieveNotes7daysTask(timestamp).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                double sad = 0;
                double happy = 0;
                double excited = 0;
                double fear = 0;
                double angry = 0;
                double bored = 0;
                if(notes.size() > 0) {
                    if (mNotes7.size() > 0) {
                        mNotes7.clear();
                    }

                    if (notes != null) {
                        mNotes7.addAll(notes);
                    }
                    int list_size = mNotes7.size();
                    Log.d(TAG, "list size: " + list_size);

                    for (int i = 0; i < list_size; i++){
                        sad = sad + mNotes7.get(i).getSad();
                        happy = happy + mNotes7.get(i).getHappy();
                        angry = angry + mNotes7.get(i).getAngry();
                        excited = excited + mNotes7.get(i).getExcited();
                        fear = fear + mNotes7.get(i).getFear();
                        bored = bored + mNotes7.get(i).getBored();
                        setViews(getView(),sad,happy,angry,excited,fear,bored);
                    }

                    Log.d(TAG, "content: " + mNotes7.get(0).getContent()); //position 0 would be the oldest entry in the list that got returned
                    Log.d(TAG, "sad: " + sad);
                }
            }
        });

        mNoteRepository.retrieveNotes30daysTask(timestamp30).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                double sad = 0;
                double happy = 0;
                double excited = 0;
                double fear = 0;
                double angry = 0;
                double bored = 0;
                if(notes.size() > 0) {
                    if (mNotes30.size() > 0) {
                        mNotes30.clear();
                    }

                    if (notes != null) {
                        mNotes30.addAll(notes);
                    }
                    int list_size = mNotes30.size();
                    Log.d(TAG, "list size: " + list_size);

                    for (int i = 0; i < list_size; i++){
                        sad = sad + mNotes30.get(i).getSad();
                        happy = happy + mNotes30.get(i).getHappy();
                        angry = angry + mNotes30.get(i).getAngry();
                        excited = excited + mNotes30.get(i).getExcited();
                        fear = fear + mNotes30.get(i).getFear();
                        bored = bored + mNotes30.get(i).getBored();
                        setViews30(getView(),sad,happy,angry,excited,fear,bored);
                    }

                    Log.d(TAG, "content: " + mNotes30.get(0).getContent()); //position 0 would be the oldest entry in the list that got returned
                    Log.d(TAG, "sad: " + sad);
                }
            }
        });
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

        double emotionSum = sad+happy+angry+excited+fear+bored;

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

        double emotionSum = sad+happy+angry+excited+fear+bored;

        happyValue.setText(df.format(happy/emotionSum)+"%");
        sadValue.setText(df.format(sad/emotionSum)+"%");
        boredValue.setText(df.format(bored/emotionSum)+"%");
        angryValue.setText(df.format(angry/emotionSum)+"%");
        excitedValue.setText(df.format(excited/emotionSum)+"%");
        fearValue.setText(df.format(fear/emotionSum)+"%");
    }
}