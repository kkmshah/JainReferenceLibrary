package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.BiodataMemoryDetailsModel;
import com.jainelibrary.model.UnitDetailsModel;

import java.io.Serializable;

public class BiodataMemoryDetailsResModel implements Serializable {

    public boolean status;
    public String message;
    public BiodataMemoryDetailsModel details = new BiodataMemoryDetailsModel();

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

    public BiodataMemoryDetailsModel getDetails() {
        return details;
    }

    public void setDetails(BiodataMemoryDetailsModel details) {
        this.details = details;
    }
}
