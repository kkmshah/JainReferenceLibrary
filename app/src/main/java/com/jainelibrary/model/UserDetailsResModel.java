package com.jainelibrary.model;

import android.util.Log;

public class UserDetailsResModel {
    boolean status;
    String message;
    boolean admin;
    UserDetailsModel data = new UserDetailsModel();

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public UserDetailsModel getData() {
        return data;
    }

    public void setData(UserDetailsModel data) {
        this.data = data;
    }

    public static class UserDetailsModel {
        String password;
        String created_date;
        String updated_date;
        String username;
        String  is_admin;

        public boolean getIs_admin() {
            return is_admin.equals("1");
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        String id;
        String name;
        String mobile;
        String email;
        String otp;


        public String getFirst_login() {
            return first_login;
        }

        public void setFirst_login(String first_login) {
            this.first_login = first_login;
        }

        public String getTill_date_login() {
            return till_date_login;
        }

        public void setTill_date_login(String till_date_login) {
            this.till_date_login = till_date_login;
        }

        public String getTotal_searching() {
            return total_searching;
        }

        public void setTotal_searching(String total_searching) {
            this.total_searching = total_searching;
        }

        public String getSeen_references() {
            return seen_references;
        }

        public void setSeen_references(String seen_references) {
            this.seen_references = seen_references;
        }

        String first_login,till_date_login,total_searching,seen_references;
        String saved_references,saved_books_references,shared_references, shared_books_references, downloaded_references,
                downloaded_books_references;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getSaved_references() {
            return saved_references;
        }

        public void setSaved_references(String saved_references) {
            this.saved_references = saved_references;
        }

        public String getSaved_books_references() {
            return saved_books_references;
        }

        public void setSaved_books_references(String saved_books_references) {
            this.saved_books_references = saved_books_references;
        }

        public String getShared_references() {
            return shared_references;
        }

        public void setShared_references(String shared_references) {
            this.shared_references = shared_references;
        }

        public String getShared_books_references() {
            return shared_books_references;
        }

        public void setShared_books_references(String shared_books_references) {
            this.shared_books_references = shared_books_references;
        }

        public String getDownloaded_references() {
            return downloaded_references;
        }

        public void setDownloaded_references(String downloaded_references) {
            this.downloaded_references = downloaded_references;
        }

        public String getDownloaded_books_references() {
            return downloaded_books_references;
        }

        public void setDownloaded_books_references(String downloaded_books_references) {
            this.downloaded_books_references = downloaded_books_references;
        }
    }
}
