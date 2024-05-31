package com.jainelibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME = "Spectrii_SharedPref";
    private static final String SHARED_PREF_KEYBOARD_NAME = "keyword_shared_pref";
    public static final String KEY_FORCE_ID = "updateid";
    public static final String KEY_APP_ID = "AppId";
    public static final String KEY_IS_FIRST_TIME = "first_time_app_installed";

    public static final String KEY_PROFILE_MODEL = "profile_model";
    public static final String KEY_IS_ADMIN = "false";
    public static final String KEY_IS_UNIQUE_TOKEN = "uniqueToken";
    public static final String KEY_IS_MAIN_DIRECTORY = "mainDirectory";
    public static final String IS_LOGIN = "login";
    public static final String IS_CODE = "code";
    public static final String IMG_URL = "code";
    public static final String USER_NAME = "uname";

    public static final String KEY_TITLE_NAME = "title_name";
    public static final String KEY_PREVIOUSTITLE_NAME = "previous_name";
    public static final String KEY_BOOK_DATA = "book_data";
    public static final String KEY_SELECT_LANGUAGE = "language";
    public static final String KEY_SAVE_HISTORY = "search_history";
    public static final String KEY_KEYWORD_HISTORY = "search_history";
    public static final String KEY_MY_SHELF = "my_shelf";
    public static final String KEY_USER_DETAILS = "user_details";
    public static final String KEY_USER_ID = "user_id";

    public static final String KEY_USER_IS_ADMIN = "user_is_admin";
    public static final String KEY_FILTER_FOCUS_BOOK_IDS = "filter_focus_book_ids";
    public static final String KEY_FILTER_BOOK_IDS = "filter_book_ids";
    public static final String KEY_YEAR_FILTER_BOOK_IDS = "year_filter_book_ids";
    public static final String KEY_FILTER_LENS_IDS = "filter_lens_ids";
    public static final String KEY_FILTER_CAT_IDS = "filter_cat_ids";
    public static final String KEY_YEAR_FILTER_CAT_IDS = "year_filter_cat_ids";
    public static final String KEY_FILTER_FOCUS_CAT_IDS = "filter_focus_cat_ids";
    public static final String KEY_FILTER_OPTION_BOOK_IDS = "filter_option_book_ids";
    public static final String KEY_MY_BOOK_COUNT = "filter_my_book_count";
    public static final String KEY_LAST_SEARCH_KEYWORD = "last_search_keyword";
    public static final String KEY_FOCUS_DATA = "focus_data";
    public static final String KEY_LENS_DATA = "lens_data";
    public static final String KEY_FILTERS_DATA = "filters_data";
    public static final String KEY_TOTAL_LENS_DATA = "total_lens_data";
    public static final String KEY_TOTAL_FOCUS_DATA = "total_focus_data";
    public static final String KEY_TOTAL_FILTERS_DATA = "total_filters_data";

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public void saveBooleanPref(String key, Boolean value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.e("saveBooleanPref", key + "::" + value);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBooleanPref(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void saveStringPref(String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringPref(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public String getStringKeyword(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_KEYBOARD_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void saveStrinKeyword(String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_KEYBOARD_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveUserDetails(UserDetailsResModel.UserDetailsModel userDetailsModel) {
        Gson gson = new Gson();
        String strBookDataModel = gson.toJson(userDetailsModel);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_DETAILS, strBookDataModel);
        editor.apply();
    }

    public void saveBookDetailsModel(String key, BookListResModel.BookDetailsModel bookDetailsModel) {
        Gson gson = new Gson();
        String strBookDataModel = gson.toJson(bookDetailsModel);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, strBookDataModel);
        editor.apply();
    }

    public void saveSearchLocalHistory(String key, ArrayList<SearchHistoryModel> searchHistoryList) {
        ArrayList<SearchHistoryModel> tempSearchHistoryList = new ArrayList<>();
        tempSearchHistoryList = searchHistoryList;
        Gson gson = new Gson();
        String strBookDataModel = gson.toJson(tempSearchHistoryList);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, strBookDataModel);
        editor.apply();
    }

    public ArrayList<SearchHistoryModel> getSearchHistoryList(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<SearchHistoryModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveSearchKeywordHistory(String key, ArrayList<SearchHistoryModel> searchHistoryList) {
        ArrayList<SearchHistoryModel> tempSearchHistoryList = new ArrayList<>();
        tempSearchHistoryList = searchHistoryList;
        Gson gson = new Gson();
        String strBookDataModel = gson.toJson(tempSearchHistoryList);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, strBookDataModel);
        editor.apply();
    }

    public ArrayList<SearchHistoryModel> getSearchkeywordHistory(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<SearchHistoryModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    public void saveMyShelfList(String key, ArrayList<String> myShelfList) {
        ArrayList<String> tempMyShelfList = new ArrayList<>();
        tempMyShelfList = myShelfList;
        Gson gson = new Gson();
        String strMyShelfImage = gson.toJson(tempMyShelfList);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, strMyShelfImage);
        editor.apply();
    }

    public ArrayList<String> getMyShelfList(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<String> getFilteredBookIdList(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void saveFilterBookIdList(String key, ArrayList<String> myShelfList) {
        ArrayList<String> tempMyShelfList = new ArrayList<>();
        tempMyShelfList = myShelfList;
        Gson gson = new Gson();
        String strMyShelfImage = gson.toJson(tempMyShelfList);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, strMyShelfImage);
        editor.apply();
    }

    public void removeFilterbooklist(String key) {


        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


    //this method will checker whether user is already logged in or not
    /*public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null) != null;
    }*/

    public void setBooleanPreference(String key, boolean value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(key), value);
        editor.apply();
    }

    public boolean getBooleanPreference(String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(String.valueOf(key), false);
    }


    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
      /*  Intent i = new Intent(ctx, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(i);*/

    }


}
