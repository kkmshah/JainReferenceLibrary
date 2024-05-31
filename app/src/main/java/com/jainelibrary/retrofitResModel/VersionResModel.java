package com.jainelibrary.retrofitResModel;

import com.google.gson.annotations.SerializedName;

public class VersionResModel {
    boolean status;
    VersionModel data = new VersionModel();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public VersionModel getData() {
        return data;
    }

    public void setData(VersionModel data) {
        this.data = data;
    }

    public class VersionModel {
        @SerializedName("updated_version")
        String current_version;

        @SerializedName("updated_version_name")
        String current_version_name;

        @SerializedName("message")
        String message;

        public String getCurrent_version() {
            return current_version;
        }

        public void setCurrent_version(String current_version) {
            this.current_version = current_version;
        }

        public String getCurrent_version_name() {
            return current_version_name;
        }

        public void setCurrent_version_name(String current_version_name) {
            this.current_version_name = current_version_name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
