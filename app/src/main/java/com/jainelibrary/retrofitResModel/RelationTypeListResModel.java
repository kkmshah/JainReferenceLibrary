package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.ParamparaFilterDataResModel;

import java.io.Serializable;
import java.util.ArrayList;

public class RelationTypeListResModel {

    boolean status;
    String message;

    ArrayList<ParamparaFilterDataResModel.RelationType> data = new ArrayList<ParamparaFilterDataResModel.RelationType>();

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

    public ArrayList<ParamparaFilterDataResModel.RelationType> getData() {
        return data;
    }

    public void setData(ArrayList<ParamparaFilterDataResModel.RelationType> data) {
        this.data = data;
    }
}
