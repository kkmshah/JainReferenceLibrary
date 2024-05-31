package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;

public class ShlokSearchResModel {
    boolean status;
    String message;
    ArrayList<ShlokResModel> data  = new ArrayList<ShlokResModel>();

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

    public ArrayList<ShlokResModel> getData() {
        return data;
    }

    public void setData(ArrayList<ShlokResModel> data) {
        this.data = data;
    }

    public  class ShlokResModel {
        String id;
        String name;
        String sutra_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSutra_count() {
            return sutra_count;
        }

        public void setSutra_count(String sutra_count) {
            this.sutra_count = sutra_count;
        }
    }
}
