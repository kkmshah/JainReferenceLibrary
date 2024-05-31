package com.jainelibrary.model;

import java.time.Year;
import java.util.ArrayList;

public class YearResModel {
    boolean status;
    String message;
    ArrayList<Year> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Year> getData() {
        return data;
    }

    public void setData(ArrayList<Year> data) {
        this.data = data;
    }

    public class  Year{
    String id, year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }}

}
