package com.jainelibrary.viewPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jainelibrary.fragment.AboutFragment;
import com.jainelibrary.fragment.ContactFragment;
import com.jainelibrary.fragment.DataUsersFragment;


public class AppInfoAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"ABOUT", "DATA & USERS", "CONTACT"};

    public AppInfoAdapter(FragmentManager fm) {
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
                return new AboutFragment();
            case 1:
                return new DataUsersFragment();
            case 2:
                return new ContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
