package com.jainelibrary.model;

import java.io.Serializable;

public class ImageFileModel implements Serializable, SliderData {

    String id;
    String file_name;
    String file_url;

    String thumb_file_url = "";
    boolean isLarge = false;


    public String getImgUrl() {
        return isLarge() ? getFile_url() : getThumb_file_url();
    }

    public String getThumb_file_url() {
        return thumb_file_url != null && !thumb_file_url.isEmpty() ? thumb_file_url :  getFile_url();
    }


    public void setThumb_file_url(String thumb_file_url) {
        this.thumb_file_url = thumb_file_url;
    }

    public boolean isLarge() {
        return isLarge;
    }

    public void setLarge(boolean large) {
        isLarge = large;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

}