package com.jainelibrary.multicheck;

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class MultiCheckGenre extends MultiCheckExpandableGroup {


    private int totalBookCount = 0;
    ArrayList<Integer> mSelectItems = new ArrayList<>();
    boolean isSelected;
    boolean isAllSelected;
    String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    private int parentPos = 0;

    public MultiCheckGenre(String title, List<Book> items, int parentPos) {
        super(title, items);
        if (items != null && items.size() > 0) {
            this.totalBookCount = items.size();

        }
        this.parentPos = parentPos;
    }

    public ArrayList<Integer> getmSelectItems() {
        return mSelectItems;
    }

    public void setmSelectItems(ArrayList<Integer> mSelectItems) {
        this.mSelectItems = mSelectItems;
    }

    public int getTotalBookCount() {
        return totalBookCount;
    }

    public void setTotalBookCount(int totalBookCount) {
        this.totalBookCount = totalBookCount;
    }

    public boolean isAllSelected() {
        return isAllSelected;
    }

    public void setAllSelected(boolean allSelected) {
        isAllSelected = allSelected;
    }
}

