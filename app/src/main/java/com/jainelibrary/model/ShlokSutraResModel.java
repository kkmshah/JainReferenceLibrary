package com.jainelibrary.model;

public class ShlokSutraResModel {
    String id;
    String sutra_book_name;
    String writer;
    String page_no;
    String editor;
    String translator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSutra_book_name() {
        return sutra_book_name;
    }

    public void setSutra_book_name(String sutra_book_name) {
        this.sutra_book_name = sutra_book_name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPage_no() {
        return page_no;
    }

    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }
}
