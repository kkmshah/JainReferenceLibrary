package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;
import java.util.List;

public class SearchOptionResModel {

    boolean status;
    String description;
    List<SearchOptionModel> data =  new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<SearchOptionModel> getData() {
        return data;
    }

    public void setData(List<SearchOptionModel> data) {
        this.data = data;
    }

    public class SearchOptionModel {
        int  id;
        String name;

        String description;
        boolean selected = true;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return description;
        }

        public void setMessage(String message) {
            this.description = message;
        }
    }
}
