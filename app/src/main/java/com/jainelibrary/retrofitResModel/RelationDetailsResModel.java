package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.BiodataMemoryDetailsModel;
import com.jainelibrary.model.RelationModel;

import java.io.Serializable;

public class RelationDetailsResModel implements Serializable {

    public boolean status;
    public String message;
    public RelationModel details;

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

    public RelationModel getDetails() {
        return details;
    }

    public void setDetails(RelationModel details) {
        this.details = details;
    }
}
