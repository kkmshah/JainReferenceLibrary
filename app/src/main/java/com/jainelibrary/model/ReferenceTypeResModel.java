package com.jainelibrary.model;

import java.util.ArrayList;

public class ReferenceTypeResModel {

    boolean status;
    ArrayList<TypeModel> data = new ArrayList<TypeModel>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<TypeModel> getData() {
        return data;
    }

    public void setData(ArrayList<TypeModel> data) {
        this.data = data;
    }

    public static class TypeModel {

        String id;
        String name;

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
    }
}
