package com.jainelibrary.multicheck;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    String id;
    boolean isSelected = false;
    private String name;
    private String keyword_count;
    private String year_count;
    private String author_name;
    private String publisher_name;
    private String book_url;
    private int parentPos;

    public Book(String name, String bookUrl, String authorName, String publisherName, int parentPos, String id, boolean isSelected , String strKeywordCount, String strYearCount) {
        this.name = name;
        this.book_url = bookUrl;
        this.author_name = authorName;
        this.publisher_name = publisherName;
        this.keyword_count = strKeywordCount;
        this.year_count = strYearCount;
        this.parentPos = parentPos;
        this.id = id;
        this.isSelected = isSelected;
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

    protected Book(Parcel in) {
        name = in.readString();
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getBook_url() {
        return book_url;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        return getName() != null ? getName().equals(book.getName()) : book.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

