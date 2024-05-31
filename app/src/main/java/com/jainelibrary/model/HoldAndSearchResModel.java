package com.jainelibrary.model;

import java.util.ArrayList;

public class HoldAndSearchResModel {
    boolean status;
    String message;
    ArrayList<HoldSearchModel> data = new ArrayList<HoldSearchModel>();

    public ArrayList<HoldSearchModel> getData() {
        return data;
    }

    public void setData(ArrayList<HoldSearchModel> data) {
        this.data = data;
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

    public class HoldSearchModel {
        String id;
        String book_id;
        String book_name;
        String keyword;
        String user_id;
        String pdf_page_no;
        String author_name;
        String editor_name;
        String publisher_name;
        String translator;

        public String getPdf_page_no() {
            return pdf_page_no;
        }

        public void setPdf_page_no(String pdf_page_no) {
            this.pdf_page_no = pdf_page_no;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getEditor_name() {
            return editor_name;
        }

        public void setEditor_name(String editor_name) {
            this.editor_name = editor_name;
        }

        public String getPublisher_name() {
            return publisher_name;
        }

        public void setPublisher_name(String publisher_name) {
            this.publisher_name = publisher_name;
        }

        public String getTranslator() {
            return translator;
        }

        public void setTranslator(String translator) {
            this.translator = translator;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
