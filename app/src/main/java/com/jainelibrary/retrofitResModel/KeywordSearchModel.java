package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;

public class KeywordSearchModel {
    public boolean status;
    public String message;
    public int total_data_count;
    public ArrayList<KeywordModel> data = new ArrayList<>();
    public int total_similar_found_count;
    public int total_similar_found_pages;
    public int total_keyword_search_count;
    public int total_keyword_search_pages;

    public int total_count;

    public int total_pages;

//    public ArrayList<KeywordModel> similar_data = new ArrayList<>();

//    public ArrayList<KeywordModel> getSimilarKeywords() {
//        return similar_data;
//    }
//
//    public void setSimilarKeywords(ArrayList<KeywordModel> similar_keywords) {
//        this.similar_data = similar_keywords;
//    }

    public static class KeywordModel{
        public String kid;
        public String name;
        public String transliteration;
        public String updated_by;
        public String updated_date;

        public Boolean is_similar;
        String keyword_cnt;
        boolean isExact;
        boolean isSimmilar;
        String strTotalSizeText;

        boolean isLoader;

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

        public boolean isLoader() {
            return isLoader;
        }

        public void setLoader(boolean loader) {
            isLoader = loader;
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

        public Boolean getIs_similar() {
            return is_similar;
        }

        public void setIs_similar(Boolean is_similar) {
            this.is_similar = is_similar;
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
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
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

    public int getTotal_data_count() {
        return total_data_count;
    }

    public void setTotal_data_count(int total_data_count) {
        this.total_data_count = total_data_count;
    }

    public int getTotal_similar_found_count() {
        return total_similar_found_count;
    }

    public void setTotal_similar_found_count(int total_similar_found_count) {
        this.total_similar_found_count = total_similar_found_count;
    }

    public int getTotal_similar_found_pages() {
        return total_similar_found_pages;
    }

    public void setTotal_similar_found_pages(int total_similar_found_pages) {
        this.total_similar_found_pages = total_similar_found_pages;
    }

    public int getTotal_keyword_search_count() {
        return total_keyword_search_count;
    }

    public void setTotal_keyword_search_count(int total_keyword_search_count) {
        this.total_keyword_search_count = total_keyword_search_count;
    }

    public int getTotal_keyword_search_pages() {
        return total_keyword_search_pages;
    }

    public void setTotal_keyword_search_pages(int total_keyword_search_pages) {
        this.total_keyword_search_pages = total_keyword_search_pages;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }


}