package com.jainelibrary.model;

import java.util.ArrayList;

public class CategoryResModel {

    boolean status;
    String message;
    String total_count;
    String description;
    ArrayList<CategoryModel> data = new ArrayList<CategoryModel>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalBooks() {
        return total_count;
    }

    public void setTotalBooks(String totalBooks) {
        this.total_count = totalBooks;
    }

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

    public ArrayList<CategoryModel> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryModel> data) {
        this.data = data;
    }

    public static class CategoryModel {

        String id;
        String name;
        ArrayList<CategoryBookModel> books = new ArrayList<>();

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

        public ArrayList<CategoryBookModel> getBooks() {
            return books;
        }

        public void setBooks(ArrayList<CategoryBookModel> books) {
            this.books = books;
        }

        public class CategoryBookModel {
            String book_id;
            String book_name;
            String author_name;
            String prakashak_name;
            String book_image;
            String book_large_image;
            String filter;
            String keyword_count;
            String year_count;
            boolean isChecked;

            public String getFilter() {
                return filter;
            }

            public String getKeyword_count() {
                return keyword_count;
            }

            public void setKeyword_count(String keyword_count) {
                this.keyword_count = keyword_count;
            }

            public String getYear_count() {
                return year_count;
            }

            public void setYear_count(String year_count) {
                this.year_count = year_count;
            }

            public String getBook_large_image() {
                return book_large_image;
            }

            public void setBook_large_image(String book_large_image) {
                this.book_large_image = book_large_image;
            }

            public void setFilter(String filter) {
                this.filter = filter;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getPublisher_name() {
                return prakashak_name;
            }

            public void setPublisher_name(String publisher_name) {
                this.prakashak_name = publisher_name;
            }

            public String getBook_image() {
                return book_image;
            }

            public void setBook_image(String book_url) {
                this.book_image = book_url;
            }

            public String getId() {
                return book_id;
            }

            public void setId(String id) {
                this.book_id = id;
            }

            public String getName() {
                return book_name;
            }

            public void setName(String name) {
                this.book_name = name;
            }

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }
        }
    }
}
