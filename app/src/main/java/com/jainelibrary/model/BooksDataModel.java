package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BooksDataModel implements Serializable {
    boolean status;
    String message;
    String id;
    String book_name;
    String editor_name;
    String publisher_name;

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

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getAavruti() {
        return aavruti;
    }

    public void setAavruti(String aavruti) {
        this.aavruti = aavruti;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getGranthmala() {
        return granthmala;
    }

    public void setGranthmala(String granthmala) {
        this.granthmala = granthmala;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    String author_name;
    String translator;
    String aavruti;
    String letter;
    String granthmala;
    String part;
    String keyword;
    String anuvadak_name;
    String lekhakName;
    String sampadak_name;
    String pageno;
    String strUniqueName;
    String strBookDetails;

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

    public ArrayList<BookImageDetailsModel> getData() {
        return data;
    }

    public void setData(ArrayList<BookImageDetailsModel> data) {
        this.data = data;
    }

    public void setStrBookDetails(String strBookDetails) {
        this.strBookDetails = strBookDetails;
    }

    public String getStrBookDetails() {
        return book_name + "," + " P." + pageno + ", " + lekhakName + "&#91;" +
                "ले" + "&#93;" + ", " + anuvadak_name + "&#91;" + "अनु" + "&#93;" + ", " + sampadak_name + "&#91;" + "संपा" + "&#93;";
    }

    ArrayList<BookImageDetailsModel> data = new ArrayList<>();

    public String getStrUniqueName() {
        return book_name + "" + keyword + "" + pageno;
    }

    public void setStrUniqueName(String strUniqueName) {
        this.strUniqueName = strUniqueName;
    }

    public String getPageno() {
        return pageno;
    }

    public void setPageno(String pageno) {
        this.pageno = pageno;
    }


    public String getLekhakName() {
        return lekhakName;
    }

    public void setLekhakName(String lekhakName) {
        this.lekhakName = lekhakName;
    }


    public ArrayList<BookImageDetailsModel> getmBookImageList() {
        return data;
    }

    public void setmBookImageList(ArrayList<BookImageDetailsModel> mBookImageList) {
        this.data = mBookImageList;
    }

    public String getAnuvadak_name() {
        return anuvadak_name;
    }

    public void setAnuvadak_name(String anuvadak_name) {
        this.anuvadak_name = anuvadak_name;
    }

    public String getSampadak_name() {
        return sampadak_name;
    }

    public void setSampadak_name(String sampadak_name) {
        this.sampadak_name = sampadak_name;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public static class BookImageDetailsModel implements Serializable {
        String page_url;
        String current_page;
        String page_no;

        String page_bytes;

        public String getPageno() {
            return page_no;
        }

        public void setPageno(String pageno) {
            this.page_no = pageno;
        }

        public String getPage_image() {
            return page_url;
        }

        public void setPage_image(String page_image) {
            this.page_url = page_image;
        }

        public String getType() {
            return current_page;
        }


        public String getPage_bytes() {
            return page_bytes;
        }

        public void setPage_bytes(String page_bytes) {
            this.page_bytes = page_bytes;
        }
    }
}
