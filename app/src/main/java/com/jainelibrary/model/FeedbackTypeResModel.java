package com.jainelibrary.model;

import java.util.ArrayList;

public class FeedbackTypeResModel {

    boolean status;
    String message;
    ArrayList<FeedbackType> types =  new ArrayList<FeedbackType>();

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


    public ArrayList<FeedbackType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<FeedbackType> types) {
        this.types = types;
    }

    public static class FeedbackType {

        int id;
        String name;

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
    }
}
