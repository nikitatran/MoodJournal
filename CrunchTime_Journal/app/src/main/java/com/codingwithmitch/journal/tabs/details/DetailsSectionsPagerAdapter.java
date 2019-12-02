/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package com.codingwithmitch.journal.tabs.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codingwithmitch.journal.R;

public class DetailsSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.details_tab_text_1, R.string.details_tab_text_2};
    private static final int NUM_TABS = 2;
    private final Context mContext;

    public DetailsSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * This is where you set the fragments to be shown in each tab.
     *
     * pre: position <= getCount()
     * post: fragments of each tab are instantiated according to position
     *
     * @param position tab number (starting from 0)
     * @return Returns the Fragment associated with a specified position.
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new NoteDetailsFragment();
                break;
            case 1:
                fragment = new SelectedNoteAnalysisFragment();
                break;
        }
        return fragment;
    }

    /**
     * pre:
     * - position <= getCount()
     * - size of TAB_TITLES must match getCount()
     * post: title of tabs will be set in view
     *
     * @param position tab number (starting from 0)
     * @return tab titles
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * Declares how many tabs will be shown.
     *
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return NUM_TABS;
    }
}