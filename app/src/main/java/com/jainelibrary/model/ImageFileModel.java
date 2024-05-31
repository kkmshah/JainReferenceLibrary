package com.jainelibrary.model;

import java.io.Serializable;

public class ImageFileModel implements Serializable, SliderData {

    String id;
    String file_name;
    String file_path;


    public String getImgUrl() {
        return  getFile_path();
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

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

}