package com.jainelibrary.model;

import java.util.ArrayList;
import java.util.List;

public class UserGuideResModel {
    boolean status;
    String message;
    ArrayList<UserGuideModel> data = new ArrayList<UserGuideModel>();

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

    public ArrayList<UserGuideModel> getData() {
        return data;
    }

    public void setData(ArrayList<UserGuideModel> data) {
        this.data = data;
    }

    public static class UserGuideModel {
        int id;
        String title;
        String content;
        String content_english;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_english() {
            return content_english;
        }

        public void setContent_english(String content_english) {
            this.content_english = content_english;
        }
    }
}
