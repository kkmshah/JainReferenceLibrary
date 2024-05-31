package com.jainelibrary.retrofitResModel;

import java.io.Serializable;

public class ReferenceResModel implements Serializable {

    public boolean status;
    public String message;

    public int type_id;
    public int reference_id;

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

    public int getTypeId() {
        return type_id;
    }

    public void setTypeId(int type_id) {
        this.type_id = type_id;
    }

    public int getReferenceId() {
        return reference_id;
    }

    public void setReferenceId(int reference_id) {
        this.reference_id = reference_id;
    }
}
