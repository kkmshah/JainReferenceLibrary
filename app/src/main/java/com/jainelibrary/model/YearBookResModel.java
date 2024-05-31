package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class YearBookResModel {

    boolean status;
    String message;
    ArrayList<YearBookList> data =  new ArrayList<>();

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


    public ArrayList<YearBookList> getData() {
        return data;
    }

    public void setData(ArrayList<YearBookList> data) {
        this.data = data;
    }

    public static class YearBookList implements Serializable {

        String name;
        ArrayList<YearBook> books =  new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<YearBook> getBooks() {
            return books;
        }

        public void setBooks(ArrayList<YearBook> books) {
            this.books = books;
        }

        public class YearBook implements Serializable {
            String book_id;
            String book_name;
            String author_name;
            String editor_name;
            String publisher_name;
            String book_year_id;
            String type_name;
            String type_value;
            String book_image;
            String pdf_page_no;
            String page_no;

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

            public String getBook_year_id() {
                return book_year_id;
            }

            public void setBook_year_id(String book_year_id) {
                this.book_year_id = book_year_id;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getType_value() {
                return type_value;
            }

            public void setType_value(String type_value) {
                this.type_value = type_value;
            }

            public String getBook_image() {
                return book_image;
            }

            public void setBook_image(String book_image) {
                this.book_image = book_image;
            }

            public String getPdf_page_no() {
                return pdf_page_no;
            }

            public void setPdf_page_no(String pdf_page_no) {
                this.pdf_page_no = pdf_page_no;
            }

            public String getPage_no() {
                return page_no;
            }

            public void setPage_no(String page_no) {
                this.page_no = page_no;
            }
        }
    }
}
