package com.codingwithmitch.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.tabs.details.DetailsSectionsPagerAdapter;

public class DetailsActivity extends AppCompatActivity {

    private String content = "";
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

        //show the edit button
        RelativeLayout editContainer = findViewById(R.id.edit_container);
        editContainer.setVisibility(View.VISIBLE);

        //edit click
        ImageButton edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteEditActivity.class);
                Note extra = getIntent().getParcelableExtra("selected_note");
                //Log.d("selected_note details", ""+extra);
                //Log.d("selected_note details", ""+extra.getHappy());

                intent.putExtra("selected_note", extra);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case RESULT_OK:
                TextView toolbar_text = findViewById(R.id.note_text_title);
                toolbar_text.setText(data.getStringExtra("title"));
                Log.d("onActivityresult","title: " + data.getStringExtra("title"));
                Log.d("onActivityresult","content: " + data.getStringExtra("content"));

                content = data.getStringExtra("content");

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultCode);
        }
    }

    public String getEditedContent(){
        if(content.length() > 0){
            return content;
        }
        return "";
    }
}

