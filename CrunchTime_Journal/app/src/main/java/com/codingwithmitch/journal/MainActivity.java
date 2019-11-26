package com.codingwithmitch.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.tabs.main.SectionsPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements NotesListsFragment.OnNoteListener

{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //private ArrayList<Note> mNotes = new ArrayList<>();
    @Override
    public void onNoteClick(int position, ArrayList<Note> mNotes) {
        //NoteRepository mNoteRepository = new NoteRepository(this);


        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);

        /*Toast.makeText(MainActivity.this, position + " clicked"
                        +"\n" +
                        "size: " + mNotes.size()

                , Toast.LENGTH_SHORT


        ).show();*/

    }
}