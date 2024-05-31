package com.jainelibrary.model;

import java.util.ArrayList;

public class YearResponseModel {

    boolean status;
    String message;
    ArrayList<YearModel> years = new ArrayList();

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

    public ArrayList<YearModel> getYears() {
        return years;
    }

    public void setYears(ArrayList<YearModel> years) {
        this.years = years;
    }

    public class YearModel {
        String id;
        String year_name;
        String reference_cnt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYear_name() {
            return year_name;
        }

        public void setYear_name(String year_name) {
            this.year_name = year_name;
        }

        public String getReference_cnt() {
            return reference_cnt;
        }

        public void setReference_cnt(String reference_cnt) {
            this.reference_cnt = reference_cnt;
        }
    }
}
