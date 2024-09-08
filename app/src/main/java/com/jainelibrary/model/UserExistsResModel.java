package com.jainelibrary.model;

public class UserExistsResModel {
    boolean status;
    String message;
    String user_id;
    String user_name;

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

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }
}
