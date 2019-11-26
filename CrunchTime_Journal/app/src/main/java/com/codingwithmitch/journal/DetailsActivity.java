package com.codingwithmitch.journal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.tabs.details.DetailsSectionsPagerAdapter;

public class DetailsActivity extends AppCompatActivity {
    //TODO: back button in toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        DetailsSectionsPagerAdapter sectionsPagerAdapter = new DetailsSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //text shown in toolbar
        TextView toolbarText = findViewById(R.id.note_text_title);
        if(getIntent().hasExtra("selected_note")) {
            Note note = getIntent().getParcelableExtra("selected_note");
            toolbarText.setText(note.getTitle());
        }

        //the back button
        ImageButton back = findViewById(R.id.toolbar_back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}