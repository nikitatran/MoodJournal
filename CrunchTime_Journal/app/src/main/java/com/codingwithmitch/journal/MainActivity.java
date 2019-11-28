package com.codingwithmitch.journal;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.tabs.main.MainSectionsPagerAdapter;
import com.codingwithmitch.journal.tabs.main.NotesListsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements NotesListsFragment.OnNoteListener
{
    private boolean wasClicked; //used to prevent multiple activities starting when item clicked > 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainSectionsPagerAdapter sectionsPagerAdapter = new MainSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wasClicked = false;
    }

    @Override
    public void onNoteClick(int position, ArrayList<Note> mNotes) {
        if(!wasClicked) {
            wasClicked = true;
            Intent intent = new Intent(this, DetailsActivity.class);
            //Intent intent = new Intent(this, NoteEditActivity.class);
            intent.putExtra("selected_note", mNotes.get(position));
            //Log.d("selected_note main", ""+mNotes.get(position).getHappy());
            startActivity(intent);
        }
    }
}