package com.jainelibrary.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BookDetailsResModel implements Serializable {

    boolean status;
    String message;

    @SerializedName("data")
    BookDetailsModels mBookDetailsModel = new BookDetailsModels();

    public BookDetailsModels getmBookDetailsModel() {
        return mBookDetailsModel;
    }

    public void setmBookDetailsModel(BookDetailsModels mBookDetailsModel) {
        this.mBookDetailsModel = mBookDetailsModel;
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


    public static class BookDetailsModels implements Serializable {
        @SerializedName("book_id")
        String book_id;
        @SerializedName("book_name")
        String book_name;
        @SerializedName("editor_name")
        String editor_name;
        @SerializedName("publisher_name")
        String publisher_name;
        @SerializedName("author_name")
        String author_name;
        @SerializedName("translator")
        String translator;
        @SerializedName("edition")
        String aavruti;
        @SerializedName("patr")
        String letter;
        @SerializedName("granthmala")
        String granthmala;
        @SerializedName("part")
        String part;
        @SerializedName("book_code")
        String code;
        @SerializedName("grade")
        String grade;
        @SerializedName("vikram_savant")
        String vikram_savant;
        @SerializedName("veer_savant")
        String veer_savant;
        @SerializedName("year_subject")
        String year_subject;

        ArrayList<BookAppendixModel> apendix = new ArrayList<>();

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getVikram_savant() {
            return vikram_savant;
        }

        public void setVikram_savant(String vikram_savant) {
            this.vikram_savant = vikram_savant;
        }

        public String getVeer_savant() {
            return veer_savant;
        }

        public void setVeer_savant(String veer_savant) {
            this.veer_savant = veer_savant;
        }

        public String getYear_subject() {
            return year_subject;
        }

        public void setYear_subject(String year_subject) {
            this.year_subject = year_subject;
        }

        public ArrayList<BookAppendixModel> getmBookAppendixModel() {
            return apendix;
        }

        public void setmBookAppendixModel(ArrayList<BookAppendixModel> mBookAppendixModel) {
            this.apendix = mBookAppendixModel;
        }

        public class BookAppendixModel {

            String aid;
            String apendix_name;
            String total_pages;
            String pdf_pages;

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getApendix_name() {
                return apendix_name;
            }

            public void setApendix_name(String apendix_name) {
                this.apendix_name = apendix_name;
            }

            public String getTotal_pages() {
                return total_pages;
            }

            public void setTotal_pages(String total_pages) {
                this.total_pages = total_pages;
            }

            public String getPdf_pages() {
                return pdf_pages;
            }

            public void setPdf_pages(String pdf_pages) {
                this.pdf_pages = pdf_pages;
            }
        }
    }
}
