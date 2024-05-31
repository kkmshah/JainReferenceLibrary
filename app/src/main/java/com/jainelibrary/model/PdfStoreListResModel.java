package com.jainelibrary.model;

import java.util.ArrayList;

public class PdfStoreListResModel {

    public boolean status;
    String message;

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    int total_pages;

    ArrayList<PdfListModel> data = new ArrayList<PdfListModel>();

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

    public ArrayList<PdfListModel> getData() {
        return data;
    }

    public void setData(ArrayList<PdfListModel> data) {
        this.data = data;
    }

    public class PdfListModel {
        String book_id;
        String book_name;
        String keyword_id;
        String author_name;
        String editor_name;
        String publisher_name;
        String translator;
        String pdf_page_no;
        String book_url;
        String pdf_url;

        public String getKeyword_id() {
            return keyword_id;
        }

        public void setKeyword_id(String keyword_id) {
            this.keyword_id = keyword_id;
        }

        public String getPdf_url() {
            return pdf_url;
        }

        public void setPdf_url(String pdf_url) {
            this.pdf_url = pdf_url;
        }

        public String getBook_url() {
            return book_url;
        }

        public void setBook_url(String book_url) {
            this.book_url = book_url;
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

        public String getPdf_page_no() {
            return pdf_page_no;
        }

        public void setPdf_page_no(String pdf_page_no) {
            this.pdf_page_no = pdf_page_no;
        }
    }
}
