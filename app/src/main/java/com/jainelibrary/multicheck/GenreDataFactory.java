package com.jainelibrary.multicheck;

import com.jainelibrary.model.CategoryResModel;

import java.util.ArrayList;
import java.util.List;

public class GenreDataFactory {
    static int count = 0;
    static ArrayList<CategoryResModel.CategoryModel> mCatLists = new ArrayList<>();

    public static List<MultiCheckGenre> makeMultiCheckGenres(ArrayList<CategoryResModel.CategoryModel> mCatList) {
        List<MultiCheckGenre> mainList = new ArrayList<>();
        mCatLists = mCatList;
        for (int i = 0; i < mCatLists.size(); i++) {
            String strTitle = mCatLists.get(i).getName();
            MultiCheckGenre multiCheckGenre = new MultiCheckGenre(strTitle, makeJazzArtists(i), i);
            mainList.add(multiCheckGenre);
        }
        return mainList;
    }

    public static int getcount() {
        return count;
    }

    public static List<Book> makeJazzArtists(int pos) {
        ArrayList<CategoryResModel.CategoryModel.CategoryBookModel> mCatBookModelList = mCatLists.get(pos).getBooks();
        ArrayList<Book> mBookList = new ArrayList<>();
        for (int i = 0; i < mCatBookModelList.size(); i++) {
            count++;
            String strTitle = mCatBookModelList.get(i).getName();
            String strId = mCatBookModelList.get(i).getId();
            String strAuthorName = mCatBookModelList.get(i).getAuthor_name();
            String strBookUrl = mCatBookModelList.get(i).getBook_url();
            String strPublisherName = mCatBookModelList.get(i).getPublisher_name();
            String strFilter = mCatBookModelList.get(i).getFilter();
            String strKeywordCount = mCatBookModelList.get(i).getKeyword_count();
            String strYearCount = mCatBookModelList.get(i).getYear_count();
            boolean isSelected = false;
            if (strFilter != null && strFilter.equalsIgnoreCase("1")){
                isSelected = true;
            }
            Book bookModel = new Book(strTitle, strBookUrl, strAuthorName, strPublisherName, pos ,strId,isSelected,strKeywordCount, strYearCount);
            mBookList.add(bookModel);
        }
        return mBookList;
    }
}

