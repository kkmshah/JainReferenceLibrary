package com.jainelibrary.retrofitResModel;

import java.io.Serializable;

public class CheckMyShelfFileNameResModel {
    public boolean status;
    public String message;

    String pdf_url;
    String pdf_image;
    String pdf_large_image;

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

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getPdf_image() {
        return pdf_image;
    }

    public void setPdf_image(String pdf_image) {
        this.pdf_image = pdf_image;
    }

    public String getPdf_large_image() {
        return pdf_large_image;
    }

    public void setPdf_large_image(String pdf_large_image) {
        this.pdf_large_image = pdf_large_image;
    }
}
