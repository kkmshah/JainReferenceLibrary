package com.jainelibrary.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyShelfResModel implements Serializable {
    boolean status;
    String message;
    ArrayList<MyShelfModel> data = new ArrayList<MyShelfModel>();

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

    public ArrayList<MyShelfModel> getData() {
        return data;
    }

    public void setData(ArrayList<MyShelfModel> data) {
        this.data = data;
    }

    public static class MyShelfModel implements Serializable {
        String id;
        String uid;


        @SerializedName("file_type")
        String fileType;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        String book_id;
        String book_name;
        String book_image;
        String book_large_image;
        String type_id,type_name;
        String type;
        String cdate;

        @SerializedName("is_note_added")
        String is_note_added;
        //String is_note_added;

        @SerializedName("pdf_file_name")
        String pdf_file_name;

        @SerializedName("pdf_file_url")
        String url;

        @SerializedName("book_pdf_file")
        String bookPdfUrl;

        String type_ref;

        @SerializedName("notes")
        String notes;

        public String getcDate() {
            return cdate;
        }

        public void setcDate(String cDate) {
            this.cdate = cDate;
        }

        public String getBookPdfUrl() {
            return bookPdfUrl;
        }

        public void setBookPdfUrl(String bookPdfUrl) {
            this.bookPdfUrl = bookPdfUrl;
        }

        public String getType_id() {
            return type_id;
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

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        String note_updated_date;
        String created_date;
        String updated_date;
        String author_name;
        String prakashak_name;
        ArrayList<MyShelfImageModel> page_no = new ArrayList<MyShelfImageModel>();

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getPrakashak_name() {
            return prakashak_name;
        }

        public void setPrakashak_name(String prakashak_name) {
            this.prakashak_name = prakashak_name;
        }

        boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getPdfFileName() {
            return pdf_file_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType_ref() {
            return type_ref;
        }

        public void setType_ref(String type_ref) {
            this.type_ref = type_ref;
        }

        public String getNotes() {
            return notes;
        }

        public String getIs_note_added() {
            return is_note_added;
        }

        public void setIs_note_added(String is_note_added) {
            this.is_note_added = is_note_added;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getNote_updated_date() {
            return note_updated_date;
        }

        public void setNote_updated_date(String note_updated_date) {
            this.note_updated_date = note_updated_date;
        }

        public ArrayList<MyShelfImageModel> getPage_no() {
            return page_no;
        }

        public void setPage_no(ArrayList<MyShelfImageModel> page_no) {
            this.page_no = page_no;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }


        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }



        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public  class MyShelfImageModel implements Serializable  {
            String id;
            String page_no;
            String url;

            public String getPage_id() {
                return id;
            }

            public void setPage_id(String page_id) {
                this.id = page_id;
            }

            public String getPage_no() {
                return page_no;
            }

            public void setPage_no(String page_no) {
                this.page_no = page_no;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }


}
