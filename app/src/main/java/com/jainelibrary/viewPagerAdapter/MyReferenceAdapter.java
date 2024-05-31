package com.jainelibrary.viewPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.jainelibrary.fragment.ActivityFragment;
import com.jainelibrary.fragment.BooksFragment;
import com.jainelibrary.fragment.FilesFragment;

public class MyReferenceAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]
            {"FILES", "BOOKS", "ACTIVITY"};

    public MyReferenceAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FilesFragment filesFragment = new FilesFragment();
                return filesFragment;
            case 1:
                BooksFragment booksFragment= new BooksFragment();
                return booksFragment;
            case 2:
                ActivityFragment activityFragment = new ActivityFragment();
                return activityFragment;
            default:
                return new FilesFragment();
        }
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
