package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.UnitDetailsModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UnitDetailsResModel implements Serializable {

    public boolean status;
    public String message;
    public UnitDetailsModel details;

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

    public UnitDetailsModel getDetails() {
        return details;
    }

    public void setDetails(UnitDetailsModel details) {
        this.details = details;
    }



}
