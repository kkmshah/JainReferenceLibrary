package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.ParamparaFilterDataResModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SamvatsBaseOnSamvatTypeDataResModel {

    boolean status;
    String message;

    ArrayList<ParamparaFilterDataResModel.Samvat> samvats = new ArrayList<ParamparaFilterDataResModel.Samvat>();

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

    public ArrayList<ParamparaFilterDataResModel.Samvat> getSamvats() {
        return samvats;
    }

    public void setSamvats(ArrayList<ParamparaFilterDataResModel.Samvat> samvats) {
        this.samvats = samvats;
    }
}
