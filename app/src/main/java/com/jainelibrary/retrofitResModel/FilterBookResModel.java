package com.jainelibrary.retrofitResModel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FilterBookResModel {
    boolean status;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    List<FilterModel> data =  new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @NonNull
    public List<FilterModel> getData() {
        return data;
    }

    public void setData(@NonNull List<FilterModel> data) {
        this.data = data;
    }

    public class FilterModel {
        int book_id;
        String book_name;
        String book_url;
        int filter;
        boolean isSelected;

        public String getFocusUrl() {
            return book_url;
        }

        public void setFocusUrl(@NonNull String book_url) {
            this.book_url = book_url;
        }

        public int getFilter() {
            return filter;
        }

        public void setFilter(int filter) {
            this.filter = filter;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getName() {
            return book_name;
        }

        public void setName(String name) {
            this.book_name = name;
        }

        public int getId() {
            return book_id;
        }

        public void setId(int id) {
            this.book_id = id;
        }
    }
}
