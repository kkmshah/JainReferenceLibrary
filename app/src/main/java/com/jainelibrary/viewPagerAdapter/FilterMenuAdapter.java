package com.jainelibrary.viewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jainelibrary.fragment.FilterCategoriesFragment;
import com.jainelibrary.fragment.FilterFocusCatFragment;
import com.jainelibrary.fragment.FilterLensFragment;

import org.jetbrains.annotations.NotNull;

public class FilterMenuAdapter extends FragmentStatePagerAdapter {

    //private String[] tabTitles = new String[]{"LENS", "FOCUS", "FILTER"};
    private String[] tabTitles = new String[]{"LENS", "FOCUS"};
    String strKeyword;

    public FilterMenuAdapter(@NonNull FragmentManager fm, @NonNull String strKeyword) {
        super(fm);
        this.strKeyword = strKeyword;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new FilterLensFragment();

            case 1:
                return new FilterFocusCatFragment("");

            case 2:
                return new FilterCategoriesFragment(strKeyword);

            default:
                return new FilterLensFragment();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
