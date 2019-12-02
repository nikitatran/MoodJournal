/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */
package com.codingwithmitch.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.tabs.details.DetailsSectionsPagerAdapter;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        /* SET UP TAB LAYOUT */
        DetailsSectionsPagerAdapter sectionsPagerAdapter = new DetailsSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        /* ----------------- */

        final RelativeLayout edit_container = findViewById(R.id.edit_container);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //Only show edit button on entry tab.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position != 0){
                    edit_container.setVisibility(View.GONE);
                }
                else edit_container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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

        //show the edit button
        RelativeLayout editContainer = findViewById(R.id.edit_container);
        editContainer.setVisibility(View.VISIBLE);

        //Initialize edit click listener
        //to send note as it originally was when selected in
        //MainActivity's RecyclerView
        ImageButton edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteEditActivity.class);

                if(getIntent().hasExtra("selected_note")) {
                    Note extra = getIntent().getParcelableExtra("selected_note");
                    intent.putExtra("selected_note", extra);
                }

                startActivityForResult(intent, 1);
            }
        });
    }

    //After user finishes editing the selected note,
    //show the edited text in view
    //and set intent extra to edited note
    //so if the user edits again without going back,
    //the edits will reflect in NoteEditActivity as well
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case RESULT_OK:
                TextView toolbar_text = findViewById(R.id.note_text_title);

                final String title = data.getStringExtra("title");
                toolbar_text.setText(title);

                content = data.getStringExtra("content");

                //reset edit onClickListener
                ImageButton edit = findViewById(R.id.edit);
                edit.setOnClickListener(null);

                // have edit onClickListener send an intent with the edited note
                // accounts for the case where more than NoteActivityEdit is called sequentially
                // without going back to MainActivity
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), NoteEditActivity.class);

                        if(getIntent().hasExtra("selected_note")) {
                            Note extra = getIntent().getParcelableExtra("selected_note");
                            extra.setContent(content);
                            extra.setTitle(title);
                            intent.putExtra("selected_note", extra);
                        }

                        startActivityForResult(intent, 1);
                    }
                });

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultCode);
        }
    }

    /**
     * Helper function used as a bridge between Activity and Fragment.
     * Should be used by fragments attached to DetailsActivity to get
     * intent extras that are returned to this activity.
     *
     * Specifically used by NotesDetailsFragment so that it can update
     * it's view with the intent extras that were returned in this activity
     *
     * pre: onActivityResult called with RESULT_OK result code,
     *  which is called after user backs out of NoteEditActivity
     * post: edited version of note content returned
     *
     * @return edited version of note content
     */
    public String getEditedContent(){
        if(content.length() > 0){
            return content;
        }
        return "";
    }
}

