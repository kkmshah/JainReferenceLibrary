package com.jainelibrary.model;

import java.util.ArrayList;

public class YearTypeResModel {

    boolean status;
    String message;
    ArrayList<YearType> types =  new ArrayList<>();

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


    public ArrayList<YearType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<YearType> types) {
        this.types = types;
    }

    public static class YearType {

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
