package com.jainelibrary.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterResModel {

    List<FilterResModelLIst> mData = new ArrayList<>();

    public List<FilterResModelLIst> getData() {
        return mData;
    }

    public FilterResModel(List<FilterResModelLIst> mData) {
        this.mData = mData;
    }

    public List<FilterResModelLIst> getFilterData(List<String> genre, List<FilterResModelLIst> mList) {
        List<FilterResModelLIst> tempList = new ArrayList<>();
        for (FilterResModelLIst movie : mList) {
            for (String g : genre) {
                if (movie.getName().equalsIgnoreCase(g)) {
                    tempList.add(movie);
                }
            }
        }
        return tempList;
    }

    public List<String> getFilterableDatas() {
        List<String> mList = new ArrayList<>();
        for (FilterResModelLIst movie : mData) {
            if (!mList.contains(movie.getName())) {
                mList.add(movie.getName());
            }
        }
        Collections.sort(mList);
        return mList;
    }

    public List<String> getHistoryData() {
        List<String> mList = new ArrayList<>();
        for (FilterResModelLIst movie : mData) {
            if (!mList.contains(movie.getName())) {
                mList.add(movie.getName());
            }
        }
        Collections.sort(mList);
        return mList;
    }

}
