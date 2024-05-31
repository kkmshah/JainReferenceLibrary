package com.jainelibrary.viewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.activity.YearFragment;
import com.jainelibrary.fragment.PramparaFragment;
import com.jainelibrary.fragment.IndexFragment;
import com.jainelibrary.fragment.ShlokFragment;

public class SearchReferenceAdapter extends FragmentStatePagerAdapter {

    boolean isHold;
    boolean isUserAdmin;
    private String[] tabTitles;



    public SearchReferenceAdapter(@NonNull FragmentManager fm, boolean isHold, boolean isUserAdmin) {
        super(fm);
        this.isHold = isHold;
        if(isUserAdmin) {
            tabTitles = new String[]{"KEYWORD", "SHLOK NO.", "INDEX", "YEAR", "PARAMPARA"};
        } else {
            tabTitles = new String[]{"KEYWORD", "SHLOK NO.", "INDEX", "YEAR"};
        }
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new KeywordsMainFragment();
            case 1:
                return new ShlokFragment();
            case 2:
                return new IndexFragment();
            case 3:
                return new YearFragment();
            case 4:
                return new PramparaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
