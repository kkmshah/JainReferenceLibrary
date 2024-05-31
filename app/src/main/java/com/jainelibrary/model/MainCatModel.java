package com.jainelibrary.model;

import java.util.ArrayList;

public class MainCatModel {
    public String catName = "";
    public String catId = "";
    public boolean isSelected = false;

    public ArrayList<SubCatModel> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(ArrayList<SubCatModel> subCatList) {
        this.subCatList = subCatList;
    }

    public ArrayList<SubCatModel> subCatList;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
