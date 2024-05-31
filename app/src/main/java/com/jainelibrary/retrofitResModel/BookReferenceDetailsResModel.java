package com.jainelibrary.retrofitResModel;

import android.graphics.pdf.PdfDocument;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BookReferenceDetailsResModel implements Serializable {

    public boolean status;
    public String message;
    public int total_count;
    public int total_pages;
    BookRefDetailsModel data = new BookRefDetailsModel();

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

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public BookRefDetailsModel getData() {
        return data;
    }

    public void setData(BookRefDetailsModel data) {
        this.data = data;
    }

    public static class BookRefDetailsModel implements Serializable {

        String book_id;
        String book_name;
        String author_name;
        String publisher_name;
        String granthmala;
        String edition;
        String language;
        String patr;
        String vikram_savant;
        String isvi_san;
        String veer_savant;
        String pdf_page_no;
        String total_images;
        String book_url;
        String pdf_url;

        public ReferenceModel reference_count_data = new ReferenceModel();
        public ReferenceTypeModel reference_type_data = new ReferenceTypeModel();

        public ArrayList<PageDiffModel> page_diffs_data = new ArrayList<>();
        public ArrayList<ReferencePageModel> reference_pages = new ArrayList<>();

        public String getBook_id() {
            return book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public String getPublisher_name() {
            return publisher_name;
        }

        public String getGranthmala() {
            return granthmala;
        }

        public String getEdition() {
            return edition;
        }

        public String getLanguage() {
            return language;
        }

        public String getPatr() {
            return patr;
        }

        public String getVikram_savant() {
            return vikram_savant;
        }

        public String getIsvi_san() {
            return isvi_san;
        }

        public String getVeer_savant() {
            return veer_savant;
        }

        public String getPdf_page_no() {
            return pdf_page_no;
        }

        public String getTotal_images() {
            return total_images;
        }

        public String getBook_url() {
            return book_url;
        }

        public String getPdf_url() {
            return pdf_url;
        }

        public ReferenceModel getReferenceModel() {
            return reference_count_data;
        }

        public ReferenceTypeModel getReferenceTypeModel() {
            return reference_type_data;
        }

        public ArrayList<PageDiffModel> getPageDiffs() {
            return page_diffs_data;
        }

        public ArrayList<ReferencePageModel> gerReferencePageModels() {
            return reference_pages;
        }

        public class ReferenceModel implements Serializable {

            String keyword_ref_count, shlok_ref_count, index_ref_count, year_ref_count, all_ref_count;

            public String getKeyword_ref_count() {
                return keyword_ref_count;
            }

            public String getShlok_ref_count() {
                return shlok_ref_count;
            }

            public String getIndex_ref_count() {
                return index_ref_count;
            }

            public String getYear_ref_count() {
                return year_ref_count;
            }

            public String getAll_ref_count() {
                return all_ref_count;
            }
        }

        public class ReferenceTypeModel implements Serializable {

            String general_count, special_count, pending_count, marked_count, notmarked_count, all_count;

            public String getGeneral_count() {
                return general_count;
            }

            public String getSpecial_count() {
                return special_count;
            }

            public String getPending_count() {
                return pending_count;
            }

            public String getMarked_count() {
                return marked_count;
            }

            public String getNotmarked_count() {
                return notmarked_count;
            }

            public String getAll_count() {
                return all_count;
            }
        }

        public class PageDiffModel implements Serializable {

            String diff, count;

            public int getDiff() {
                return Integer.parseInt(diff);
            }

            public int getCount() {
                return Integer.parseInt(count);
            }
        }

        public class ReferencePageModel implements Serializable {

            String pdf_page_no, page_no;

            public ArrayList<ReferencePageRefModel> references = new ArrayList<>();

            public int getPdf_page_no() {
                return Integer.parseInt(pdf_page_no);
            }

            public int getPage_no() {
                return Integer.parseInt(page_no);
            }

            public ArrayList<ReferencePageRefModel> getReferencePageRefModels() {
                return references;
            }

            public class ReferencePageRefModel implements Serializable {

                String id, type_id;
                String type, type_name, type_value;
                String pdf_page_no, page_no;
                String x1, y1, x2, y2, is_checked;
                String created_date, updated_date;

                public int getId() {
                    return Integer.parseInt(id);
                }

                public int getType_id() {
                    return Integer.parseInt(type_id);
                }

                public String getType() { return type; }

                public String getType_name() {
                    return type_name;
                }

                public String getType_value() {
                    return type_value;
                }

                public int getPdf_page_no() {
                    return Integer.parseInt(pdf_page_no);
                }

                public int getPage_no() {
                    return Integer.parseInt(page_no);
                }

                public int getX1() {
                    return Integer.parseInt(x1);
                }

                public int getY1() {
                    return Integer.parseInt(y1);
                }

                public int getX2() {
                    return Integer.parseInt(x2);
                }

                public int getY2() {
                    return Integer.parseInt(y2);
                }

                public int getIs_checked() {
                    return Integer.parseInt(is_checked);
                }

                public String getCreated_date() {
                    return created_date;
                }

                public String getUpdated_date() {
                    return updated_date;
                }
            }
        }
    }
}
