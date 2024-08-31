package com.jainelibrary.retrofitResModel;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class BookListResModel implements Serializable {

    public boolean status;
    public String message;
    public String type_id;
    ArrayList<BookDetailsModel> data = new ArrayList<>();

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

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public ArrayList<BookDetailsModel> getData() {
        return data;
    }

    public void setData(ArrayList<BookDetailsModel> data) {
        this.data = data;
    }


    public static class BookDetailsModel implements Serializable {

        public ArrayList<BookPageModel> page_nos = new ArrayList<>();

        public ArrayList<BookPageModel> books = new ArrayList<>();

        File tempImageArray;

        public File getTempImageArray() {
            return tempImageArray;
        }

        public void setTempImageArray(File tempImageArray) {
            this.tempImageArray = tempImageArray;
        }

        String index_book_id;
        String index_keyword_id;
        String index_name;
        String prakashak_name;
        String book_image;
        String book_large_image;
        String book_id;
        String book_name;
        String pdf_page_no;
        String author_name;
        String editor_name;
        String publisher_name;
        String translator;
        String keyword;
        String keywordId;
        String page_no;
        String name;
        String flag;
        String strSutraName;
        String user_id;
        String id;
        String language;
        String patr;
        String edition;
        String year_subject;
        String vikram_savant;
        String isvi_san;
        String veer_savant;
        String granthmala;
        String grade;
        String pdf_link;
        String book_code;
        String part;
        String uncounted_page_no;
        String book_url;
        String type_name;

        //Added for get the hold type of book
        String type_id;

        public ArrayList<BookPageModel> getPageModels() {
            return page_nos;
        }

        public void setPageModels(ArrayList<BookPageModel> pageModels) {
            this.page_nos = pageModels; }

        public String getIndex_book_id() {
            return index_book_id;
        }

        public void setIndex_book_id(String index_book_id) {
            this.index_book_id = index_book_id;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
        String year;

        public String getIndex_keyword_id() {
            return index_keyword_id;
        }
        public void setIndex_keyword_id(String index_keyword_id) {
            this.index_keyword_id = index_keyword_id;
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

        public String getBook_url() {
            return book_image;
        }
        public void setBook_url(String book_url) {
            this.book_image = book_url;
        }
        public String getKeywordId() {
            return keywordId;
        }
        public void setKeywordId(String keywordId) {
            this.keywordId = keywordId;
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
        public String getKeyword() {
            return keyword;
        }
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
        public String getPage_no() {
            return page_no;
        }
        public void setPage_no(String page_no) {
            this.page_no = page_no;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getFlag() {
            return flag;
        }
        public void setFlag(String flag) {
            this.flag = flag;
        }
        public String getStrSutraName() {
            return strSutraName;
        }
        public void setStrSutraName(String strSutraName) {
            this.strSutraName = strSutraName;
        }
        public String getUser_id() {
            return user_id;
        }
        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        // Book_details Param //
        public String getUncounted_page_no() {
            return uncounted_page_no;
        }

        public void setUncounted_page_no(String uncounted_page_no) {
            this.uncounted_page_no = uncounted_page_no;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        ArrayList<BookAppendixModel> apendix = new ArrayList<>();

        public String getBook_code() {
            return book_code;
        }

        public void setBook_code(String book_code) {
            this.book_code = book_code;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
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

        public String getPdf_link() {
            return pdf_link;
        }

        public void setPdf_link(String pdf_link) {
            this.pdf_link = pdf_link;
        }


        public String getBook_image() {
            return book_image;
        }

        public void setBook_image(String book_image) {
            this.book_image = book_image;
        }

        public String getBook_large_image() {
            return book_large_image;
        }

        public void setBook_large_image(String book_large_image) {
            this.book_large_image = book_large_image;
        }

        public ArrayList<BookAppendixModel> getApendix() {
            return apendix;
        }

        public void setApendix(ArrayList<BookAppendixModel> apendix) {
            this.apendix = apendix;
        }


        public ArrayList<BookAppendixModel> getAppendix() {
            return apendix;
        }

        public void setAppendix(ArrayList<BookAppendixModel> appendix) {
            this.apendix = appendix;
        }


        public ArrayList<BookPageModel> getBooks() {
            return books;
        }

        public void setBooks(ArrayList<BookPageModel> books) {
            this.books = books;
        }

        public static class BookAppendixModel implements Serializable {

            @SerializedName("id")
            String id;

            @SerializedName("name")
            String name;

            String total_pages;

            @SerializedName("pdf_page_no")
            String pdf_page_no;

            @SerializedName("page_no")
            String page_no;

            public String getStarting_page() {
                return page_no;
            }

            public void setStarting_page(String starting_page) {
                this.page_no = starting_page;
            }

            public String getAid() {
                return id;
            }

            public void setAid(String aid) {
                this.id = aid;
            }

            public String getApendix_name() {
                return name;
            }

            public void setApendix_name(String apendix_name) {
                this.name = apendix_name;
            }

            public String getTotal_pages() {
                return total_pages;
            }

            public void setTotal_pages(String total_pages) {
                this.total_pages = total_pages;
            }

            public String getPdf_pages() {
                return pdf_page_no;
            }

            public void setPdf_pages(String pdf_pages) {
                this.pdf_page_no = pdf_pages;
            }
        }


        public class BookPageModel implements Serializable {

            String page_no, pdf_page_no,
                    keyword,
                    book_keyword_id;
            String book_id;
            String book_name;
            String author_name;
            String editor_name;
            String publisher_name;
            String book_year_id;
            String type_name;
            String type_value;
            String book_image;

            String book_large_image;
            public ArrayList<Book> page_nos = new ArrayList<>();

            public String getPage_no() {
                return page_no;
            }

            public void setPage_no(String page_no) {
                this.page_no = page_no;
            }

            public String getPdf_page_no() {
                return pdf_page_no;
            }

            public void setPdf_page_no(String pdf_page_no) {
                this.pdf_page_no = pdf_page_no;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getBook_keyword_id() {
                return book_keyword_id;
            }

            public void setBook_keyword_id(String book_keyword_id) {
                this.book_keyword_id = book_keyword_id;
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

            public String getBook_large_image() {
                return book_large_image;
            }

            public void setBook_large_image(String book_large_image) {
                this.book_large_image = book_large_image;
            }

            public ArrayList<Book> getPage_nos() {
                return page_nos;
            }

            public void setPage_nos(ArrayList<Book> page_nos) {
                this.page_nos = page_nos;
            }

            public class Book implements Serializable {
                String page_no;
                String pdf_page_no;

                public String getPage_no() {
                    return page_no;
                }

                public void setPage_no(String page_no) {
                    this.page_no = page_no;
                }

                public String getPdf_page_no() {
                    return pdf_page_no;
                }

                public void setPdf_page_no(String pdf_page_no) {
                    this.pdf_page_no = pdf_page_no;
                }
            }
        }
    }

}



/*
        public String id;
        public String book_code;
        public String type;
        public String name;
        public String part;
        public String author_id;
        public String editor_id;
        public String publisher_id;
        public String granthmala;
        public String language;
        public String patr;
        public String edition;
        public String uncounted_page_no;
        public String pdf_page_no;
        public String grade;
        public String active_notes;
        public String year_subject;
        public String vikram_savant;
        public String isvi_san;
        public String veer_savant;
        public String created_by;
        public String created_date;
        public String updated_by;
        public String updated_date;
        public String keyword_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook_code() {
            return book_code;
        }

        public void setBook_code(String book_code) {
            this.book_code = book_code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getTranslator() {
            return translator;
        }

        public void setTranslator(String translator) {
            this.translator = translator;
        }

        public String getEditor_id() {
            return editor_id;
        }

        public void setEditor_id(String editor_id) {
            this.editor_id = editor_id;
        }

        public String getPublisher_id() {
            return publisher_id;
        }

        public void setPublisher_id(String publisher_id) {
            this.publisher_id = publisher_id;
        }

        public String getGranthmala() {
            return granthmala;
        }

        public void setGranthmala(String granthmala) {
            this.granthmala = granthmala;
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

        public String getUncounted_page_no() {
            return uncounted_page_no;
        }

        public void setUncounted_page_no(String uncounted_page_no) {
            this.uncounted_page_no = uncounted_page_no;
        }

        public String getPdf_page_no() {
            return pdf_page_no;
        }

        public void setPdf_page_no(String pdf_page_no) {
            this.pdf_page_no = pdf_page_no;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getActive_notes() {
            return active_notes;
        }

        public void setActive_notes(String active_notes) {
            this.active_notes = active_notes;
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

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
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

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getKeyword_id() {
            return keyword_id;
        }

        public void setKeyword_id(String keyword_id) {
            this.keyword_id = keyword_id;
        }

        public String getPage_no() {
            return page_no;
        }

        public void setPage_no(String page_no) {
            this.page_no = page_no;
        }*/





