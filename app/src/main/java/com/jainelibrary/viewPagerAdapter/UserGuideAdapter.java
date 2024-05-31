package com.jainelibrary.viewPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jainelibrary.fragment.UgStorageFragment;
import com.jainelibrary.fragment.UgSearchFragment;
import com.jainelibrary.fragment.UgSettingsFragment;

public class UserGuideAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"SETTING", "SEARCH", "STORAGE"};

    public UserGuideAdapter(FragmentManager fm) {
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
                UgSettingsFragment ugSettingsFragment = new UgSettingsFragment();
                return ugSettingsFragment;
            case 1:
                UgSearchFragment ugSearchFragment = new UgSearchFragment();
                return ugSearchFragment;
            case 2:
                UgStorageFragment ugStorageFragment = new UgStorageFragment();
                return ugStorageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
