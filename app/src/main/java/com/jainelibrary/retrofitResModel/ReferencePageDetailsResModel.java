package com.jainelibrary.retrofitResModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ReferencePageDetailsResModel implements Serializable {

    public boolean status;
    RefPageDetailsModel data = new RefPageDetailsModel();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RefPageDetailsModel getData() {
        return data;
    }

    public void setData(RefPageDetailsModel data) {
        this.data = data;
    }

    public static class RefPageDetailsModel implements Serializable {

        String id;
        String book_id;
        String type_id;
        String type;
        String type_name;
        String type_value;
        String book_name;
        String pdf_link;
        String page_no;
        String pdf_page_no;
        String is_checked;
        String x1;
        String x2;
        String y1;
        String y2;

        ReferenceModel prev_reference;
        ReferenceModel next_reference;

        String page_bytes;

        public int getId() {
            return Integer.parseInt(id);
        }

        public int getBook_id() {
            return Integer.parseInt(book_id);
        }

        public int getType_id() {
            return Integer.parseInt(type_id);
        }

        public String getType() {
            return type;
        }

        public String getType_name() {
            return type_name;
        }

        public String getType_value() {
            return type_value;
        }

        public String getBook_name() {
            return book_name;
        }

        public String getPdf_link() {
            return pdf_link;
        }

        public int getPage_no() {
            return Integer.parseInt(page_no);
        }

        public int getPdf_page_no() {
            return Integer.parseInt(pdf_page_no);
        }

        public int getIs_checked() {
            return Integer.parseInt(is_checked);
        }

        public int getX1() {
            return Integer.parseInt(x1);
        }

        public int getX2() {
            return Integer.parseInt(x2);
        }

        public int getY1() {
            return Integer.parseInt(y1);
        }

        public int getY2() {
            return Integer.parseInt(y2);
        }

        public ReferenceModel getPrev_reference() {
            return prev_reference;
        }

        public ReferenceModel getNext_reference() {
            return next_reference;
        }

        public String getPage_bytes() {
            return page_bytes;
        }

        public static class ReferenceModel implements Serializable {

            String id;
            String type_id;

            public int getId() {
                return Integer.parseInt(id);
            }

            public int getType_id() {
                return Integer.parseInt(type_id);
            }
        }
    }
}
