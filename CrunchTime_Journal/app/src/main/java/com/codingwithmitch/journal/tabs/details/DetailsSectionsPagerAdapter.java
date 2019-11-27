package com.codingwithmitch.journal.tabs.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.codingwithmitch.journal.NoteDetailsFragment;

import com.codingwithmitch.journal.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DetailsSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.details_tab_text_1, R.string.details_tab_text_2, R.string.details_tab_text_3};
    private final Context mContext;

    public DetailsSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.d("getItem", position + " called");
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new NoteDetailsFragment();
                break;
            case 1:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}