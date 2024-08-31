package com.jainelibrary.utils;

import android.content.Context;

import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;

import java.util.ArrayList;

import io.paperdb.Book;
import io.paperdb.Paper;

public class StorageManager {

    private  static boolean hasInit = false;


    private static final String BOOK_CATEGORY_LIST = "book_category_list";
    private static final String BOOK_LIST = "book_list";

    private static final String MY_SHELF_REF_LIST = "my_shelf_ref_list";
    private static final String BOOK_APPENDIX = "book_appendix";

    private static final String SELF_HOLD_AND_SEARCH = "self_hold_and_search";

    public static void init(Context context) {
        if(hasInit) {
            return;
        }
        hasInit = true;
        // Should be initialized once in Application.onCreate()
        Paper.init(context);
    }
    public static Book response(String key) {
        return Paper.book("response_"+cacheKey(key));
    }

    public static void saveCategoryListResponse(ArrayList<CategoryResModel.CategoryModel> categoryList, String key) {

        response(BOOK_CATEGORY_LIST).write(cacheKey(key), categoryList);
    }

    public static ArrayList<CategoryResModel.CategoryModel> getCategoryListResponse(String key) {
        return response(BOOK_CATEGORY_LIST).read(cacheKey(key));
    }

    public static void saveBookListResponse(ArrayList<PdfStoreListResModel.PdfListModel> list, String key) {
        response(BOOK_LIST).write(cacheKey(key), list);
    }

    public static ArrayList<PdfStoreListResModel.PdfListModel> getBookListResponse(String key) {
        return response(BOOK_LIST).read(cacheKey(key));
    }

    public static void setMyShelfListResponse(ArrayList<MyShelfResModel.MyShelfModel> list, String key) {
        response(MY_SHELF_REF_LIST).write(cacheKey(key), list);
    }

    public static ArrayList<MyShelfResModel.MyShelfModel> getMyShelfListResponse(String key, boolean skipCache) {
        return skipCache ? null : response(MY_SHELF_REF_LIST).read(cacheKey(key));
    }

    public static void setBookAppendixDetails(ArrayList<BookListResModel.BookDetailsModel> list, String key) {
        response(BOOK_APPENDIX).write(cacheKey(key), list);
    }

    public static ArrayList<BookListResModel.BookDetailsModel> getBookAppendixDetails(String key) {
        return response(BOOK_APPENDIX).read(cacheKey(key));
    }

    public static void setHoldSearchKeywordList(ArrayList<BookListResModel.BookDetailsModel> list, String key) {
        response(SELF_HOLD_AND_SEARCH).write(cacheKey(key), list);
    }

    public static ArrayList<BookListResModel.BookDetailsModel> getHoldSearchKeywordList(String key, boolean skipCache) {
        return skipCache ? null : response(SELF_HOLD_AND_SEARCH).read(cacheKey(key));
    }

    private static String cacheKey(String key) {
        if(key ==null || key.isEmpty()) {
            return "row";
        }
        return  "row_"+key;
    }
}
