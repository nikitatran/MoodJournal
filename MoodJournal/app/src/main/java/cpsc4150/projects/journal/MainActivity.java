/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */
package cpsc4150.projects.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.projects.journal.R;

import cpsc4150.projects.journal.models.Note;
import cpsc4150.projects.journal.tabs.CustomSwipeDirectionViewPager;
import cpsc4150.projects.journal.tabs.main.MainSectionsPagerAdapter;
import cpsc4150.projects.journal.tabs.main.NotesListsFragment;
import cpsc4150.projects.journal.tabs.SwipeDirection;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements NotesListsFragment.OnNoteListener
{
    private boolean wasClicked; //used to prevent multiple activities starting when item clicked > 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* SET UP TAB LAYOUT */
        final MainSectionsPagerAdapter sectionsPagerAdapter = new MainSectionsPagerAdapter(this, getSupportFragmentManager());
        final CustomSwipeDirectionViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){ //first tab
                    viewPager.setAllowedSwipeDirection(SwipeDirection.LEFT);
                }
                else if (position == sectionsPagerAdapter.getCount() - 1){ //last tab
                    viewPager.setAllowedSwipeDirection(SwipeDirection.RIGHT);
                }
                else viewPager.setAllowedSwipeDirection(SwipeDirection.DEFAULT);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wasClicked = false; //reset click listener when user returns from another fragment/activity
    }

    /**
     * From interface OnNoteListener defined in NotesListsFragment.
     * Defines what happens when a note in the RecyclerView was clicked.
     *
     * pre: note was clicked in RecyclerView
     * post: note details page opens
     *
     * @param position position of item clicked in RecyclerView
     * @param mNotes list of notes
     */
    @Override
    public void onNoteClick(int position, ArrayList<Note> mNotes) {
        if(!wasClicked) {
            wasClicked = true;
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("selected_note", mNotes.get(position));
            startActivity(intent);
        }
    }
}