package com.jainelibrary.model;
import java.util.ArrayList;
public class MarkingBookModel {

    boolean status;
    String message;
    ArrayList<MarkingBook> data = new ArrayList<>();

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

    public ArrayList<MarkingBook> getData() {
        return data;
    }

    public void setData(ArrayList<MarkingBook> data) {
        this.data = data;
    }

    public class MarkingBook {
        String page_url;
        ArrayList<BookDetails> book_details = new ArrayList<>();

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public ArrayList<BookDetails> getBook_details() {
            return book_details;
        }

        public void setBook_details(ArrayList<BookDetails> book_details) {
            this.book_details = book_details;
        }

        public class BookDetails {
            String book_id;
            String book_name;
            String author_name;
            String editor_name;
            String pdf_link;
            String publisher_name;
            String translator;
            String language;
            String patr;
            String edition;
            String year_subject;
            String vikram_savant;
            String isvi_san;
            String veer_savant;
            String granthmala;
            String grade;
            String x1;
            String x2;
            String y1;
            String y2;

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

            public String getPdf_link() {
                return pdf_link;
            }

            public void setPdf_link(String pdf_link) {
                this.pdf_link = pdf_link;
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

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getPatr() {
                return patr;
            }

            public void setPatr(String patr) {
                this.patr = patr;
            }

            public String getEdition() {
                return edition;
            }

            public void setEdition(String edition) {
                this.edition = edition;
            }

            public String getYear_subject() {
                return year_subject;
            }

            public void setYear_subject(String year_subject) {
                this.year_subject = year_subject;
            }

            public String getVikram_savant() {
                return vikram_savant;
            }

            public void setVikram_savant(String vikram_savant) {
                this.vikram_savant = vikram_savant;
            }

            public String getIsvi_san() {
                return isvi_san;
            }

            public void setIsvi_san(String isvi_san) {
                this.isvi_san = isvi_san;
            }

            public String getVeer_savant() {
                return veer_savant;
            }

            public void setVeer_savant(String veer_savant) {
                this.veer_savant = veer_savant;
            }

            public String getGranthmala() {
                return granthmala;
            }

            public void setGranthmala(String granthmala) {
                this.granthmala = granthmala;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getX1() {
                return x1;
            }

            public void setX1(String x1) {
                this.x1 = x1;
            }

            public String getX2() {
                return x2;
            }

            public void setX2(String x2) {
                this.x2 = x2;
            }

            public String getY1() {
                return y1;
            }

            public void setY1(String y1) {
                this.y1 = y1;
            }

            public String getY2() {
                return y2;
            }

            public void setY2(String y2) {
                this.y2 = y2;
            }
        }
    }
}
