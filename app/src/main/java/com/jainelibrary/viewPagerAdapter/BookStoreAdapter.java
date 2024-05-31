package com.jainelibrary.viewPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jainelibrary.fragment.GalleryFragment;
import com.jainelibrary.fragment.SearchFragment;
import com.jainelibrary.fragment.ViewListFragment;

public class BookStoreAdapter extends FragmentStatePagerAdapter {

    //private String[] tabTitles = new String[]{"GALLERY", "VIEWLIST", "SEARCH"};
    private String[] tabTitles = new String[]{"GALLERY",  "SEARCH"};

    public BookStoreAdapter(FragmentManager fm) {
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
                GalleryFragment galleryFragment = new GalleryFragment();
                return galleryFragment;
            case 1:
                /*ViewListFragment viewListFragment = new ViewListFragment();
                return viewListFragment;
            case 2:*/
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
