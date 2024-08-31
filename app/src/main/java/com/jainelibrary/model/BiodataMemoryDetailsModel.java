package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BiodataMemoryDetailsModel implements Serializable {
    String id;
    String number;
    String sequence;
    String unit_id;
    String type;
    String type_id;
    String samvat_id;
    String samvat_year_type;
    String samvat_month_id;
    String samvat_month_tithi_id;

    String samvat_year_name;
    String date;
    String day;
    String location;
    String location_english;
    String note;
    String note_english;

    String unit_type;
    String unit_type_id;
    String unit_name;

    String type_name;

    String date_details;
    String image_count;

    String cnt_count;
    String mtr_count;
    String status_name;
    String sect_name;
    ArrayList reference_book_pages;

    ArrayList<ReferenceBook> reference_books = new ArrayList<ReferenceBook>();
    ArrayList<ImageFileModel> files = new ArrayList<ImageFileModel>();

    ArrayList<CntMtrModel> cnt_list = new ArrayList<CntMtrModel>();

    ArrayList<CntMtrModel> mtr_list = new ArrayList<CntMtrModel>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getSamvat_id() {
        return samvat_id;
    }

    public void setSamvat_id(String samvat_id) {
        this.samvat_id = samvat_id;
    }

    public String getSamvat_year_type() {
        return samvat_year_type;
    }

    public void setSamvat_year_type(String samvat_year_type) {
        this.samvat_year_type = samvat_year_type;
    }

    public String getSamvat_month_id() {
        return samvat_month_id;
    }

    public void setSamvat_month_id(String samvat_month_id) {
        this.samvat_month_id = samvat_month_id;
    }

    public String getSamvat_year_name() {
        return samvat_year_name;
    }

    public void setSamvat_year_name(String samvat_year_name) {
        this.samvat_year_name = samvat_year_name;
    }

    public String getSamvat_month_tithi_id() {
        return samvat_month_tithi_id;
    }

    public void setSamvat_month_tithi_id(String samvat_month_tithi_id) {
        this.samvat_month_tithi_id = samvat_month_tithi_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_english() {
        return location_english;
    }

    public void setLocation_english(String location_english) {
        this.location_english = location_english;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_english() {
        return note_english;
    }

    public void setNote_english(String note_english) {
        this.note_english = note_english;
    }

    public String getUnit_type() {
        return unit_type;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public String getUnit_type_id() {
        return unit_type_id;
    }

    public void setUnit_type_id(String unit_type_id) {
        this.unit_type_id = unit_type_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getDate_details() {
        return date_details;
    }

    public void setDate_details(String date_details) {
        this.date_details = date_details;
    }

    public String getCnt_count() {
        return cnt_count;
    }

    public void setCnt_count(String cnt_count) {
        this.cnt_count = cnt_count;
    }

    public String getMtr_count() {
        return mtr_count;
    }

    public void setMtr_count(String mtr_count) {
        this.mtr_count = mtr_count;
    }

    public String getImage_count() {
        return image_count;
    }

    public void setImage_count(String image_count) {
        this.image_count = image_count;
    }

    public ArrayList getReference_book_pages() {
        return reference_book_pages;
    }

    public void setReference_book_pages(ArrayList reference_book_pages) {
        this.reference_book_pages = reference_book_pages;
    }

    public ArrayList<ReferenceBook> getReference_books() {
        return reference_books;
    }

    public void setReference_books(ArrayList<ReferenceBook> reference_books) {
        this.reference_books = reference_books;
    }

    public ArrayList<ImageFileModel> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<ImageFileModel> files) {
        this.files = files;
    }

    public ArrayList<CntMtrModel> getCnt_list() {
        return cnt_list;
    }

    public void setCnt_list(ArrayList<CntMtrModel> cnt_list) {
        this.cnt_list = cnt_list;
    }

    public ArrayList<CntMtrModel> getMtr_list() {
        return mtr_list;
    }

    public void setMtr_list(ArrayList<CntMtrModel> mtr_list) {
        this.mtr_list = mtr_list;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getSect_name() {
        return sect_name;
    }

    public void setSect_name(String sect_name) {
        this.sect_name = sect_name;
    }

    public class ReferenceBook implements Serializable {
        String category_id;
        String category_name;
        String book_id;
        String book_name;
        String author_name;
        String editor_name;
        String publisher_name;
        String translator;
        String pdf_url;
        String book_image;
        String book_large_image;
        String book_page_image_url;
        String book_pdf_page_no;
        ArrayList<ReferencePage> reference_pages = new ArrayList<ReferencePage>();

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
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

        public String getPdf_url() {
            return pdf_url;
        }

        public void setPdf_url(String pdf_url) {
            this.pdf_url = pdf_url;
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

        public String getBook_page_image_url() {
            return book_page_image_url;
        }

        public void setBook_page_image_url(String book_page_image_url) {
            this.book_page_image_url = book_page_image_url;
        }

        public String getBook_pdf_page_no() {
            return book_pdf_page_no;
        }

        public void setBook_pdf_page_no(String book_pdf_page_no) {
            this.book_pdf_page_no = book_pdf_page_no;
        }

        public ArrayList<ReferencePage> getReference_pages() {
            return reference_pages;
        }

        public void setReference_pages(ArrayList<ReferencePage> reference_pages) {
            this.reference_pages = reference_pages;
        }

        public class ReferencePage implements Serializable {
            String pdf_page_no;
            String page_no;
            ArrayList<Reference> references = new ArrayList<Reference>();

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

            public ArrayList<Reference> getReferences() {
                return references;
            }

            public void setReferences(ArrayList<Reference> references) {
                this.references = references;
            }

            public class Reference implements Serializable {
                String id;
                String x1;
                String y1;
                String x2;
                String y2;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getX1() {
                    return x1;
                }

                public void setX1(String x1) {
                    this.x1 = x1;
                }

                public String getY1() {
                    return y1;
                }

                public void setY1(String y1) {
                    this.y1 = y1;
                }

                public String getX2() {
                    return x2;
                }

                public void setX2(String x2) {
                    this.x2 = x2;
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
}
