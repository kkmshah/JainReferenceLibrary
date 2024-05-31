package com.jainelibrary.model;

public class BooksModel {

    String id;
    String book_name;
    String page_no1;
    String page_no2;
    String keyword;
    String total_pg;

    public String getTotal_pg() {
        return total_pg;
    }

    public void setTotal_pg(String total_pg) {
        this.total_pg = total_pg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getPage_no1() {
        return page_no1;
    }

    public void setPage_no1(String page_no1) {
        this.page_no1 = page_no1;
    }

    public String getPage_no2() {
        return page_no2;
    }

    public void setPage_no2(String page_no2) {
        this.page_no2 = page_no2;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
