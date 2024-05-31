package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;

public class KeywordSearchModel {
    public boolean status;
    public String message;
    public int total_data;
    public ArrayList<KeywordModel> data = new ArrayList<>();
    public ArrayList<KeywordModel> similar_data = new ArrayList<>();

    public ArrayList<KeywordModel> getSimilarKeywords() {
        return similar_data;
    }

    public void setSimilarKeywords(ArrayList<KeywordModel> similar_keywords) {
        this.similar_data = similar_keywords;
    }

    public static class KeywordModel{
        public String kid;
        public String name;
        public String transliteration;
        public String updated_by;
        public String updated_date;
        String keyword_cnt;
        boolean isExact;
        boolean isSimmilar;
        String strTotalSizeText;

        public String getStrTotalSizeText() {
            return strTotalSizeText;
        }

        public void setStrTotalSizeText(String strTotalSizeText) {
            this.strTotalSizeText = strTotalSizeText;
        }

        public boolean isExact() {
            return isExact;
        }

        public void setExact(boolean exact) {
            isExact = exact;
        }

        public boolean isSimmilar() {
            return isSimmilar;
        }

        public void setSimmilar(boolean simmilar) {
            isSimmilar = simmilar;
        }

        public String getKeyword_cnt() {
            return keyword_cnt;
        }

        public void setKeyword_cnt(String keyword_cnt) {
            this.keyword_cnt = keyword_cnt;
        }

        public String getId() {
            return kid;
        }

        public void setId(String id) {
            this.kid = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTransliteration() {
            return transliteration;
        }

        public void setTransliteration(String transliteration) {
            this.transliteration = transliteration;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }
    }

    public int getTotal_count() {
        return total_data;
    }

    public void setTotal_count(int total_count) {
        this.total_data = total_count;
    }

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

    public ArrayList<KeywordModel> getData() {
        return data;
    }

    public void setData(ArrayList<KeywordModel> data) {
        this.data = data;
    }
}