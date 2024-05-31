package com.jainelibrary.retrofitResModel;

import java.util.ArrayList;

public class IndexSearchResModel {

    boolean status;
    String message;
    ArrayList<IndexResModel> data = new ArrayList<>();

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

    public ArrayList<IndexResModel> getData() {
        return data;
    }

    public void setData(ArrayList<IndexResModel> data) {
        this.data = data;
    }

    public class IndexResModel {
        String count;
        String id,created_by,updated_date,updated_by,data_added_by,name;

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getData_added_by() {
            return data_added_by;
        }

        public void setData_added_by(String data_added_by) {
            this.data_added_by = data_added_by;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getIndex_keyword_id() {
            return index_keyword_id;
        }

        public void setIndex_keyword_id(String index_keyword_id) {
            this.index_keyword_id = index_keyword_id;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getIndex_name() {
            return index_name;
        }

        public void setIndex_name(String index_name) {
            this.index_name = index_name;
        }

        public String getPrakashak_name() {
            return prakashak_name;
        }

        public void setPrakashak_name(String prakashak_name) {
            this.prakashak_name = prakashak_name;
        }

        public String getBook_image() {
            return book_image;
        }

        public void setBook_image(String book_image) {
            this.book_image = book_image;
        }

        String index_keyword_id;
        String book_id;
        String index_name;
        String book_name;
        String author_name;
        String prakashak_name;
        String book_image;

        public String getIndex_id() {
            return index_keyword_id;
        }

        public void setIndex_id(String index_id) {
            this.index_keyword_id = index_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getPublisher_name() {
            return prakashak_name;
        }

        public void setPublisher_name(String publisher_name) {
            this.prakashak_name = publisher_name;
        }

        public String getBook_url() {
            return book_image;
        }

        public void setBook_url(String book_url) {
            this.book_image = book_url;
        }

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
