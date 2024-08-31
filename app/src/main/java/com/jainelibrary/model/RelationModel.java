package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RelationModel implements Serializable {
    String id;
    String number;
    String sequence;
    String unit_id;
    String relation_name;
    String relation_id;
    String unit_id1;
    String unit_id2;
    String unit_name1;
    String unit_name2;
    String unit1_type_id;
    String unit2_type_id;

    String unit_type_name1;
    String unit_status_name1;
    String unit_sect_name1;
    String unit_type_name2;
    String unit_status_name2;
    String unit_sect_name2;
    String unit_relation_name1;
    String unit_relation_name2;
    String cnt_count;
    String mtr_count;
    String book_references_count;
    String file_count;
    ArrayList<ImageFileModel> relations_files;
    ArrayList reference_book_pages;
    ArrayList cnt_details;
    ArrayList mtr_details;

    //
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

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getUnit_id1() {
        return unit_id1;
    }

    public void setUnit_id1(String unit_id1) {
        this.unit_id1 = unit_id1;
    }

    public String getUnit_id2() {
        return unit_id2;
    }

    public void setUnit_id2(String unit_id2) {
        this.unit_id2 = unit_id2;
    }

    public String getUnit_name1() {
        return unit_name1;
    }

    public void setUnit_name1(String unit_name1) {
        this.unit_name1 = unit_name1;
    }

    public String getUnit_name2() {
        return unit_name2;
    }

    public void setUnit_name2(String unit_name2) {
        this.unit_name2 = unit_name2;
    }

    public String getUnit1_type_id() {
        return unit1_type_id;
    }

    public void setUnit1_type_id(String unit1_type_id) {
        this.unit1_type_id = unit1_type_id;
    }

    public String getUnit2_type_id() {
        return unit2_type_id;
    }

    public void setUnit2_type_id(String unit2_type_id) {
        this.unit2_type_id = unit2_type_id;
    }

    public String getUnit_status_name1() {
        return unit_status_name1;
    }

    public void setUnit_status_name1(String unit_status_name1) {
        this.unit_status_name1 = unit_status_name1;
    }

    public String getUnit_sect_name1() {
        return unit_sect_name1;
    }

    public void setUnit_sect_name1(String unit_sect_name1) {
        this.unit_sect_name1 = unit_sect_name1;
    }

    public String getUnit_status_name2() {
        return unit_status_name2;
    }

    public void setUnit_status_name2(String unit_status_name2) {
        this.unit_status_name2 = unit_status_name2;
    }

    public String getUnit_sect_name2() {
        return unit_sect_name2;
    }

    public void setUnit_sect_name2(String unit_sect_name2) {
        this.unit_sect_name2 = unit_sect_name2;
    }

    public String getUnit_relation_name1() {
        return unit_relation_name1;
    }

    public void setUnit_relation_name1(String unit_relation_name1) {
        this.unit_relation_name1 = unit_relation_name1;
    }

    public String getUnit_relation_name2() {
        return unit_relation_name2;
    }

    public void setUnit_relation_name2(String unit_relation_name2) {
        this.unit_relation_name2 = unit_relation_name2;
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

    public String getBook_references_count() {
        return book_references_count;
    }

    public void setBook_references_count(String book_references_count) {
        this.book_references_count = book_references_count;
    }

    public String getFile_count() {
        return file_count;
    }

    public void setFile_count(String file_count) {
        this.file_count = file_count;
    }

    public ArrayList<ImageFileModel> getRelations_files() {
        return relations_files;
    }

    public void setRelations_files(ArrayList<ImageFileModel> relations_files) {
        this.relations_files = relations_files;
    }

    public ArrayList<ReferenceBook> getReference_books() {
        return reference_books;
    }

    public void setReference_books(ArrayList<ReferenceBook> reference_books) {
        this.reference_books = reference_books;
    }

    public ArrayList getReference_book_pages() {
        return reference_book_pages;
    }

    public void setReference_book_pages(ArrayList reference_book_pages) {
        this.reference_book_pages = reference_book_pages;
    }

    public ArrayList getCnt_details() {
        return cnt_details;
    }

    public void setCnt_details(ArrayList cnt_details) {
        this.cnt_details = cnt_details;
    }

    public ArrayList getMtr_details() {
        return mtr_details;
    }

    public void setMtr_details(ArrayList mtr_details) {
        this.mtr_details = mtr_details;
    }

    public String getUnit_type_name1() {
        return unit_type_name1;
    }

    public void setUnit_type_name1(String unit_type_name1) {
        this.unit_type_name1 = unit_type_name1;
    }

    public String getUnit_type_name2() {
        return unit_type_name2;
    }

    public void setUnit_type_name2(String unit_type_name2) {
        this.unit_type_name2 = unit_type_name2;
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
        ArrayList<ReferenceBook.ReferencePage> reference_pages = new ArrayList<ReferenceBook.ReferencePage>();

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

        public ArrayList<ReferenceBook.ReferencePage> getReference_pages() {
            return reference_pages;
        }

        public void setReference_pages(ArrayList<ReferenceBook.ReferencePage> reference_pages) {
            this.reference_pages = reference_pages;
        }

        public class ReferencePage implements Serializable {
            String pdf_page_no;
            String page_no;
            ArrayList<ReferenceBook.ReferencePage.Reference> references = new ArrayList<ReferenceBook.ReferencePage.Reference>();

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

            public ArrayList<ReferenceBook.ReferencePage.Reference> getReferences() {
                return references;
            }

            public void setReferences(ArrayList<ReferenceBook.ReferencePage.Reference> references) {
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