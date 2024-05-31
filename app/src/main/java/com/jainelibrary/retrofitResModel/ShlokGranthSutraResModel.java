package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;

public class ShlokGranthSutraResModel {

    boolean status;
    String message;
    int total_pages;
    ArrayList<ShlokGranthSutraModel> data = new ArrayList<>();

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

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<ShlokGranthSutraModel> getData() {
        return data;
    }

    public void setData(ArrayList<ShlokGranthSutraModel> data) {
        this.data = data;
    }

    public class ShlokGranthSutraModel {
        String name;
        String id;
        String book_count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook_count() {
            return book_count;
        }

        public void setBook_count(String book_count) {
            this.book_count = book_count;
        }
    }

}
